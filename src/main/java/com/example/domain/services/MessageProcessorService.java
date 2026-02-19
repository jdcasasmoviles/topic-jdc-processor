package com.example.domain.services;

import com.example.domain.models.MessageData;
import com.example.domain.models.MessageResult;
import com.example.domain.ports.MessagePublisherPort;
import com.example.domain.ports.MessageValidationPort;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.util.Objects;

public class MessageProcessorService {
    private final MessagePublisherPort publisher;
    private final MessageValidationPort validator;

    public MessageProcessorService(MessagePublisherPort publisher, MessageValidationPort validator) {
        this.publisher = Objects.requireNonNull(publisher, "Publisher is required");
        this.validator = Objects.requireNonNull(validator, "Validator is required");
    }

    public Uni<MessageResult> processMessage(MessageData message) {
        return Uni.createFrom()
                .item(message)
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .invoke(validator::validate)
                .onItem()
                .transformToUni(publisher::publish)
                .onFailure()
                .recoverWithUni(this::handleFailure);
    }

    private Uni<MessageResult> handleFailure(Throwable error) {
        return Uni.createFrom()
                .item(MessageResult.builder()
                        .status("FAILED")
                        .processedAt(java.time.Instant.now())
                        .build());
    }
}