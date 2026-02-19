package com.example.domain.ports;

import com.example.domain.models.MessageData;
import com.example.domain.models.MessageResult;
import io.smallrye.mutiny.Uni;

public interface MessagePublisherPort {
    Uni<MessageResult> publish(MessageData message);
}