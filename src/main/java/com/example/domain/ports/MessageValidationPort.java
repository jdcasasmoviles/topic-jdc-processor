package com.example.domain.ports;
import com.example.domain.models.MessageData;

public interface MessageValidationPort {
    void validate(MessageData message);
}
