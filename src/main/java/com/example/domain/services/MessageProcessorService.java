package com.example.domain.services;

import com.example.domain.models.MessageData;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

public class MessageProcessorService {
    private static final Logger LOG = Logger.getLogger(MessageProcessorService.class);
    public Uni<Void> processMessage(MessageData message) {
        return Uni.createFrom()
                .item(message)
                .onItem().invoke(msg ->
                        LOG.info("processMessage: " + msg.getId())
                )
                .replaceWithVoid();  // Convierte explícitamente a Uni<Void>
    }
}