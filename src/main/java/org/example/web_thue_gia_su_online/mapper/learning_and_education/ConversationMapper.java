package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", source = "student.displayName")
    @Mapping(target = "tutorId", source = "tutor.id")
    @Mapping(target = "tutorName", source = "tutor.displayName")
    ConversationResponse toResponse(Conversation conversation);
}
