package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Lesson;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    Lesson toEntity(LessonCreationRequest request);

    @Mapping(target = "courseId", source = "course.id")
    LessonResponse toResponse(Lesson entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Lesson entity, LessonUpdateRequest request);
}
