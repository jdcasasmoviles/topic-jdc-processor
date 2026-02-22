package com.example.infrastructure.input.rest;

import com.example.api.TaskApi;
import com.example.api.model.TaskRequest;
import com.example.api.model.TaskResponse;
import com.example.application.dtos.MessageRequestDTO;
import com.example.application.dtos.MessageResponseDTO;
import com.example.application.usecases.ProcessServiceImpl;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import java.time.Duration;

@ApplicationScoped
public class MessageController implements TaskApi {

    private static final Logger LOG = Logger.getLogger(MessageController.class);

    @Inject
    ProcessServiceImpl processMessageUseCase;

    @Override
    public TaskResponse processMessage(TaskRequest request) {
        LOG.info("Processing message request: " + request);
        TaskResponse  res = new TaskResponse();
        return Uni.createFrom()
                .item(request)
                .map(this::mapToDTO)
                .flatMap(processMessageUseCase::execute)   // usar flatMap en lugar de chain
                .map(this::mapToResponse)
                .onFailure()
                .recoverWithItem(throwable -> {
                  return res;
                }).await().atMost(Duration.ofSeconds(3));
    }

    private MessageRequestDTO mapToDTO(TaskRequest request) {
        MessageRequestDTO dto = new MessageRequestDTO();
        dto.setNombre(request.getName());
        dto.setDescripcion(request.getDescription());
        return dto;
    }
    private TaskResponse mapToResponse(MessageResponseDTO dto) {
        TaskResponse response = new TaskResponse();
        response.setId(dto.getPartition());
        response.setStatus(TaskResponse.StatusEnum.valueOf(dto.getStatus()));
        return response;
    }

}