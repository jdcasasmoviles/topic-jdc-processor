package com.example.infrastructure.output.kafka.avro;

import com.example.avro.MessageAvro;
import com.example.domain.models.MessageData;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageAvroMapper {

    public MessageAvro toAvro(MessageData message) {
        return MessageAvro.newBuilder()
                .setId(message.getId())
                .setContent(message.getContent())
                .setSource(message.getSource())
                .setTimestamp(message.getTimestamp().toEpochMilli())
                .setPriority(message.getPriority())
                .build();
    }

    public MessageData toDomain(MessageAvro avro) {
        return new MessageData.Builder()
                .id(avro.getId())
                .content(avro.getContent())
                .source(avro.getSource())
                .timestamp(java.time.Instant.ofEpochMilli(avro.getTimestamp()))
                .priority(avro.getPriority())
                .build();
    }
}