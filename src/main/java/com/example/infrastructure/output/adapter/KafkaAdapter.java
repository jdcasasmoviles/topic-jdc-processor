package com.example.infrastructure.output.adapter;

import com.example.avro.MessageAvro;
import com.example.domain.ports.EventPort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Message;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import io.smallrye.reactive.messaging.MutinyEmitter;
import org.jboss.logging.Logger;
@ApplicationScoped
public class KafkaAdapter  implements EventPort {

    private static final Logger LOG = Logger.getLogger(KafkaAdapter.class);
    @Inject
    @Channel("topic-jdc")
    MutinyEmitter<MessageAvro> emitter;

    @Override
    public Uni<Void> publish(MessageAvro messageAvro) {
        LOG.info("publish: " + messageAvro.getId());
        return Uni.createFrom()
                .item(messageAvro)
                .chain(this::sendToKafka)
                .onFailure().recoverWithUni(error -> {
                    LOG.error("publish  error: " + error.getMessage());
                    return Uni.createFrom().voidItem();
                });
    }

    public Uni<Void> sendToKafka(MessageAvro data) {
        LOG.info("sendToKafka");
        Message<MessageAvro> message = Message.of(data).addMetadata(OutgoingKafkaRecordMetadata.<MessageAvro>builder()
                .build());
        LOG.info("sendToKafka llenados data");
        return emitter.sendMessage(message).flatMap(item -> {
            LOG.info("sendToKafka a topico -> topic-jdc");
            return Uni.createFrom().item(item);
        }).onFailure().recoverWithUni(error -> {
            LOG.error("sendToKafka  error: " + error.getMessage());
            return Uni.createFrom().voidItem();
        });
    }
}
