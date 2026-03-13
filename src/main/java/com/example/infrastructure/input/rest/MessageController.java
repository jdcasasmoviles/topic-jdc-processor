package com.example.infrastructure.input.rest;

import com.example.api.TaskApi;
import com.example.api.model.TaskRequest;
import com.example.api.model.TaskResponse;
import com.example.application.dtos.MessageRequestDTO;
import com.example.application.dtos.MessageResponseDTO;
import com.example.application.usecases.ProcessServiceImpl;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.exceptions.CircuitBreakerOpenException;
import org.jboss.logging.Logger;
import java.util.concurrent.TimeoutException;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

@ApplicationScoped
public class MessageController implements TaskApi {

    private static final Logger LOG = Logger.getLogger(MessageController.class);

    @Inject
    ProcessServiceImpl processMessageUseCase;

    @Override
    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(5000)
    @CircuitBreaker(
            requestVolumeThreshold = 4,
            failureRatio = 0.75,
            delay = 10000
    )
    @RunOnVirtualThread
    public TaskResponse processMessage(TaskRequest request) {
        long startTime = System.currentTimeMillis();
        LOG.infov("Processing message request: {0}", request);
        return Uni.createFrom()
                .item(request)
                .map(this::mapToDTO)
                .flatMap(dto -> processMessageUseCase.execute(dto))
                .map(this::mapToResponse)
                .invoke(response -> {
                    long elapsed = System.currentTimeMillis() - startTime;
                    LOG.infov("Successfully processed: {0} in {1}ms", response.getId(), elapsed);
                })
                .onFailure()
                .recoverWithItem(throwable -> handleError(request, throwable, startTime))
                .await()
                .indefinitely(); // Espera indefinidamente, @Timeout cortará si excede
    }

    private TaskResponse handleError(TaskRequest request, Throwable throwable, long startTime) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (throwable instanceof TimeoutException) {
            LOG.errorv("Timeout after {0}ms for: {1} (exceeded 5000ms)",
                    elapsed, request.getName());
            return createErrorResponse(504, "Request timeout after " + elapsed + "ms");
        }

        if (throwable instanceof CircuitBreakerOpenException) {
            LOG.errorv("Circuit breaker open for: {0}", request.getName());
            return createErrorResponse(503, "Service temporarily unavailable");
        }

        LOG.errorv(throwable, "Failed after {0}ms for: {1}", elapsed, request.getName());
        return createErrorResponse(500, throwable.getMessage());
    }

    private TaskResponse createErrorResponse(Integer id, String message) {
        TaskResponse response = new TaskResponse();
        response.setId(id);
        response.setStatus(TaskResponse.StatusEnum.FAILED);
        return response;
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