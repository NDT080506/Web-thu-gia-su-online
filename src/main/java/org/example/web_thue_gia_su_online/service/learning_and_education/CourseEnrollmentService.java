package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;

public interface CourseEnrollmentService {
    CourseEnrollmentResponse enrollCourse(CourseEnrollmentCreationRequest request);

    CourseEnrollmentResponse getEnrollment(String id);

    List<CourseEnrollmentResponse> getEnrollmentsByStudent();

    List<CourseEnrollmentResponse> getEnrollmentsByCourse(String courseId);

    CourseEnrollmentResponse updateEnrollment(String id);

    CourseEnrollmentResponse completeEnrollment(String id);

    void deleteEnrollment(String id);
}
