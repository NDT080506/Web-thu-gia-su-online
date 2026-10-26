package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseEnrollmentMapper {

    @Mapping(target = "course.id", source = "courseId")
    CourseEnrollment toEntity(CourseEnrollmentCreationRequest request);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    CourseEnrollmentResponse toResponse(CourseEnrollment entity);
}
