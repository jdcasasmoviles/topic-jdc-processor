package com.example.application.usecases;

import com.example.application.dtos.MessageRequestDTO;
import com.example.application.dtos.MessageResponseDTO;
import com.example.application.mappers.MessageMapper;
import com.example.domain.services.MessageProcessorService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;

@ApplicationScoped
public class ProcessMessageUseCase {

    @Inject
    MessageProcessorService processorService;

    @Inject
    Validator validator;

    @Inject
    MessageMapper mapper;

    public Uni<MessageResponseDTO> execute(MessageRequestDTO request) {
        // Validate DTO
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Validation failed");

            return Uni.createFrom()
                    .item(MessageResponseDTO.failure(request.getId(), errorMessage));
        }

        // Map and process
        var messageData = mapper.toDomain(request);

        return processorService.processMessage(messageData)
                .map(mapper::toResponse)
                .onFailure()
                .recoverWithItem(error ->
                        MessageResponseDTO.failure(request.getId(), error.getMessage()));
    }
}