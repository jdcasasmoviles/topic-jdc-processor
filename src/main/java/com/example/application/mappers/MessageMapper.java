package com.example.application.mappers;
import com.example.application.dtos.MessageRequestDTO;
import com.example.application.dtos.MessageResponseDTO;
import com.example.domain.models.MessageData;
import com.example.domain.models.MessageResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "jakarta")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "timestamp", expression = "java(java.time.Instant.now())")
    MessageData toDomain(MessageRequestDTO dto);

    @Mapping(target = "messageId", source = "messageId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "processedAt", source = "processedAt")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "partition", source = "partition")
    @Mapping(target = "offset", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    MessageResponseDTO toResponse(MessageResult result);
}
