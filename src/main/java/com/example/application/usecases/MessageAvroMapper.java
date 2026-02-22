package com.example.application.usecases;

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
                .setPriority(message.getPriority())
                .build();
    }

}