package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    Course toEntity(CourseCreationRequest request);

    @Mapping(target = "tutorId", source = "tutor.id")
    CourseResponse toResponse(Course entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Course entity, CourseUpdateRequest request);
}
