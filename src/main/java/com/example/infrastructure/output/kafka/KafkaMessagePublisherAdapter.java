package com.example.infrastructure.output.kafka;

import com.example.avro.MessageAvro;
import com.example.domain.models.MessageData;
import com.example.domain.models.MessageResult;
import com.example.domain.ports.MessagePublisherPort;
import com.example.infrastructure.output.kafka.avro.MessageAvroMapper;
import com.example.infrastructure.util.Constants;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.jboss.logging.Logger;

import java.time.Instant;

@ApplicationScoped
public class KafkaMessagePublisherAdapter implements MessagePublisherPort {

    private static final Logger LOG = Logger.getLogger(KafkaMessagePublisherAdapter.class);

    @Inject
    @Channel("topic-jdc")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1000)
    Emitter<MessageAvro> emitter;

    @Inject
    MessageAvroMapper avroMapper;

    @Override
    public Uni<MessageResult> publish(MessageData message) {
        LOG.info("Publishing message to topic-jdc: " + message.getId());

        return Uni.createFrom()
                .item(message)
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .map(avroMapper::toAvro)
                .chain(this::sendToKafka)
                .onFailure()
                .invoke(error -> LOG.error("Failed to publish message", error))
                .onItemOrFailure()
                .transform((result, error) -> buildResult(message, result, error));
    }

    private Uni<MessageResult> sendToKafka(MessageAvro avro) {
        return Uni.createFrom().item(() -> {
            try {
                Message<MessageAvro> kafkaMessage = Message.of(avro)
                        .addMetadata(OutgoingKafkaRecordMetadata.builder()
                                .withTopic(Constants.Kafka.TOPIC_JDC)
                                .withKey(avro.getId())
                                .withHeaders(new RecordHeaders()
                                        .add("source", avro.getSource().getBytes())
                                        .add("priority", String.valueOf(avro.getPriority()).getBytes()))
                                .build());

                emitter.send(kafkaMessage); // ✅ no devuelve nada
                return buildResultFromMetadata(kafkaMessage); // construyes tu resultado
            } catch (Exception e) {
                throw new RuntimeException("Error sending to Kafka", e);
            }
        });
    }

    private MessageResult buildResultFromMetadata(Message<MessageAvro> message) {
        var metadata = message.getMetadata(OutgoingKafkaRecordMetadata.class).orElse(null);

        return MessageResult.builder()
                .status("SUCCESS")
                .processedAt(Instant.now())
                .topic(metadata != null ? metadata.getTopic() : Constants.Kafka.TOPIC_JDC)
                .partition(metadata != null ? metadata.getPartition() : 0)
                .build();
    }

    private MessageResult buildResult(MessageData message, MessageResult success, Throwable error) {
        if (error != null) {
            return MessageResult.builder()
                    .messageId(message.getId())
                    .status("FAILED")
                    .processedAt(Instant.now())
                    .build();
        }
        return MessageResult.builder()
                .messageId(success.getMessageId())
                .status(success.getStatus())
                .processedAt(success.getProcessedAt())
                .topic(success.getTopic())
                .partition(success.getPartition())
                .build();
    }
}