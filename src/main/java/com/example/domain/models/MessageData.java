package com.example.domain.models;

import java.time.Instant;
import java.util.Objects;

public class MessageData {
    private String id;
    private String content;
    private String source;
    private Instant timestamp;
    private Integer priority;

    // Constructor por defecto necesario para MapStruct
    public MessageData() {}

    // Constructor con Builder
    private MessageData(Builder builder) {
        this.id = builder.id;
        this.content = builder.content;
        this.source = builder.source;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.priority = builder.priority != null ? builder.priority : 1;
    }

    // Getters y Setters (añadir setters para MapStruct)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public static class Builder {
        private String id;
        private String content;
        private String source;
        private Instant timestamp;
        private Integer priority;

        public Builder id(String id) { this.id = id; return this; }
        public Builder content(String content) { this.content = content; return this; }
        public Builder source(String source) { this.source = source; return this; }
        public Builder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        public Builder priority(Integer priority) { this.priority = priority; return this; }

        public MessageData build() {
            Objects.requireNonNull(id, "ID is required");
            Objects.requireNonNull(content, "Content is required");
            Objects.requireNonNull(source, "Source is required");
            return new MessageData(this);
        }
    }

    // Método de fábrica para crear desde DTO (opcional)
    public static MessageData fromRequest(String id, String content, String source, Integer priority) {
        return new Builder()
                .id(id)
                .content(content)
                .source(source)
                .priority(priority)
                .timestamp(Instant.now())
                .build();
    }
}