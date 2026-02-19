package com.example.domain.models;

import java.time.Instant;

public class MessageResult {
    private final String messageId;
    private final String status;
    private final Instant processedAt;
    private final String topic;
    private final Integer partition;

    private MessageResult(Builder builder) {
        this.messageId = builder.messageId;
        this.status = builder.status;
        this.processedAt = builder.processedAt;
        this.topic = builder.topic;
        this.partition = builder.partition;
    }

    // Getters
    public String getMessageId() { return messageId; }
    public String getStatus() { return status; }
    public Instant getProcessedAt() { return processedAt; }
    public String getTopic() { return topic; }
    public Integer getPartition() { return partition; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String messageId;
        private String status;
        private Instant processedAt;
        private String topic;
        private Integer partition;

        public Builder messageId(String messageId) { this.messageId = messageId; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder processedAt(Instant processedAt) { this.processedAt = processedAt; return this; }
        public Builder topic(String topic) { this.topic = topic; return this; }
        public Builder partition(Integer partition) { this.partition = partition; return this; }
        public MessageResult build() {
            return new MessageResult(this);
        }
    }
}