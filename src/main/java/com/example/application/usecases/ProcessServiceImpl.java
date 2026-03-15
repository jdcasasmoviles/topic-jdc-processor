package com.example.application.usecases;

import com.example.application.dtos.MessageRequestDTO;
import com.example.application.dtos.MessageResponseDTO;
import com.example.application.ports.ServicePort;
import com.example.domain.models.MessageData;
import com.example.domain.ports.EventPort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProcessServiceImpl implements ServicePort {

    @Inject
    EventPort eventPort;

    @Inject
    MessageAvroMapper messageAvroMapper;

    public Uni<MessageResponseDTO> execute(MessageRequestDTO request) {
        MessageData messageData = MessageData.fromRequest("id","content","source",1);
        return eventPort.publish(messageAvroMapper.toAvro(messageData))
                .map(respo->{
                    MessageResponseDTO messageResponseDTO =
                            MessageResponseDTO.success("","",3,Long.MIN_VALUE);
                    return messageResponseDTO;
                });
    }

}