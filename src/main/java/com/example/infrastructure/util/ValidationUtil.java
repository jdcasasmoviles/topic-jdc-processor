package com.example.infrastructure.util;

import com.example.domain.models.MessageData;
import com.example.domain.ports.MessageValidationPort;
import com.example.infrastructure.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ValidationUtil implements MessageValidationPort {

    @Override
    public void validate(MessageData message) {
        if (message == null) {
            throw new BusinessException("Message cannot be null");
        }

        validateId(message.getId());
        validateContent(message.getContent());
        validateSource(message.getSource());
        validatePriority(message.getPriority());
    }

    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new BusinessException("Message ID cannot be empty");
        }
        if (id.length() < 3 || id.length() > 50) {
            throw new BusinessException("Message ID must be between 3 and 50 characters");
        }
        if (!id.matches("^[a-zA-Z0-9_-]+$")) {
            throw new BusinessException("Message ID contains invalid characters");
        }
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("Message content cannot be empty");
        }
        if (content.length() > 1000) {
            throw new BusinessException("Message content cannot exceed 1000 characters");
        }
    }

    private void validateSource(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new BusinessException("Message source cannot be empty");
        }
        if (source.length() > 100) {
            throw new BusinessException("Message source cannot exceed 100 characters");
        }
    }

    private void validatePriority(Integer priority) {
        if (priority == null) {
            return; // Optional field
        }
        if (priority < 1 || priority > 10) {
            throw new BusinessException("Priority must be between 1 and 10");
        }
    }
}