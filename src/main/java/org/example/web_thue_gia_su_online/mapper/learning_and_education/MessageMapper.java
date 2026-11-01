package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Message;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "conversation.id", source = "conversationId")
    Message toEntity(MessageCreationRequest request);

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderName", source = "sender.displayName")
    MessageResponse toResponse(Message entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Message entity, MessageUpdateRequest request);
}
