package com.example.infrastructure.input.rest;

import com.example.api.MessagesApi;
import com.example.api.model.MessageRequest;
import com.example.api.model.MessageResponse;
import com.example.application.dtos.MessageRequestDTO;
import com.example.application.dtos.MessageResponseDTO;
import com.example.application.usecases.ProcessMessageUseCase;
import com.example.infrastructure.exception.BusinessException;
import com.example.infrastructure.exception.ErrorResponse;
import com.example.infrastructure.util.Constants;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.Flow;

@Path("/api/v1/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageController implements MessagesApi {

    private static final Logger LOG = Logger.getLogger(MessageController.class);

    @Inject
    ProcessMessageUseCase processMessageUseCase;

    @Override
    public MessageResponse processMessage(MessageRequest messageRequest) {
        LOG.info("Processing message request: " + messageRequest);
        MessageResponse  res = new MessageResponse();
        return Uni.createFrom()
                .item(messageRequest)
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .map(this::mapToDTO)
                .flatMap(processMessageUseCase::execute)   // usar flatMap en lugar de chain
                .map(this::mapToResponse)
                .onFailure()
                .recoverWithItem(throwable -> {
                  return res;
                }).await().atMost(Duration.ofSeconds(3));

    }

    private MessageRequestDTO mapToDTO(MessageRequest request) {
        MessageRequestDTO dto = new MessageRequestDTO();
        dto.setId(request.getId());
        dto.setContent(request.getContent());
        dto.setSource(request.getSource());
        dto.setPriority(request.getPriority());
        return dto;
    }

    private MessageResponse mapToResponse(MessageResponseDTO dto) {
        MessageResponse response = new MessageResponse();
        response.setMessageId(dto.getMessageId());
        response.setStatus(MessageResponse.StatusEnum.valueOf(dto.getStatus()));
        response.setProcessedAt(OffsetDateTime.from(dto.getProcessedAt()));
        response.setTopic(dto.getTopic());
        response.setPartition(dto.getPartition());
        response.setOffset(dto.getOffset());
        response.setErrorMessage(dto.getErrorMessage());
        return response;
    }

    private Throwable handleException(Throwable error) {
        LOG.error("Error processing message", error);

        if (error instanceof ConstraintViolationException) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(ErrorResponse.of("Validation failed", error.getMessage()))
                            .build()
            );
        } else if (error instanceof BusinessException) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(ErrorResponse.of("Business error", error.getMessage()))
                            .build()
            );
        } else if (error.getMessage() != null && error.getMessage().contains("timeout")) {
            throw new WebApplicationException(
                    Response.status(Response.Status.GATEWAY_TIMEOUT)
                            .entity(ErrorResponse.of("Service timeout", error.getMessage()))
                            .build()
            );
        } else if (error.getMessage() != null && error.getMessage().contains("rate limit")) {
            throw new WebApplicationException(
                    Response.status(429) // Too Many Requests
                            .entity(ErrorResponse.of("Rate limit exceeded", error.getMessage()))
                            .build()
            );
        } else if (error.getMessage() != null && error.getMessage().contains("not found")) {
            throw new WebApplicationException(
                    Response.status(Response.Status.NOT_FOUND)
                            .entity(ErrorResponse.of("Resource not found", error.getMessage()))
                            .build()
            );
        } else {
            throw new WebApplicationException(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(ErrorResponse.of("Internal server error", error.getMessage()))
                            .build()
            );
        }
    }
}