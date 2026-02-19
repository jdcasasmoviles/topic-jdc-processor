package com.example.application.dtos;

import java.time.Instant;

public class MessageResponseDTO {
    private String messageId;
    private String status;
    private Instant processedAt;
    private String topic;
    private Integer partition;
    private Long offset;
    private String errorMessage;

    // Getters and Setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getProcessedAt() { return processedAt; }
    public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public Integer getPartition() { return partition; }
    public void setPartition(Integer partition) { this.partition = partition; }
    public Long getOffset() { return offset; }
    public void setOffset(Long offset) { this.offset = offset; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public static MessageResponseDTO success(String messageId, String topic, Integer partition, Long offset) {
        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessageId(messageId);
        response.setStatus("SUCCESS");
        response.setProcessedAt(Instant.now());
        response.setTopic(topic);
        response.setPartition(partition);
        response.setOffset(offset);
        return response;
    }

    public static MessageResponseDTO failure(String messageId, String errorMessage) {
        MessageResponseDTO response = new MessageResponseDTO();
        response.setMessageId(messageId);
        response.setStatus("FAILED");
        response.setProcessedAt(Instant.now());
        response.setErrorMessage(errorMessage);
        return response;
    }
}