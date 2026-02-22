package com.example.application.usecases;

import com.example.application.dtos.MessageRequestDTO;
import com.example.application.dtos.MessageResponseDTO;
import com.example.application.ports.ServicePort;
import com.example.domain.models.MessageData;
import com.example.domain.ports.EventPort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Validator;

@ApplicationScoped
public class ProcessServiceImpl implements ServicePort {

    @Inject
    EventPort eventPort;

    @Inject
    Validator validator;

    @Inject
    MessageAvroMapper messageAvroMapper;

    public Uni<MessageResponseDTO> execute(MessageRequestDTO request) {
        // Validate DTO
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Validation failed");

            return Uni.createFrom()
                    .item(MessageResponseDTO.failure(request.getNombre(), errorMessage));
        }

        MessageData messageData = MessageData.fromRequest("id","content","source",1);

        return eventPort.publish(messageAvroMapper.toAvro(messageData))
                .map(respo->{
                    MessageResponseDTO messageResponseDTO =
                            MessageResponseDTO.success("","",3,Long.MIN_VALUE);
                    return messageResponseDTO;
                });
    }

}