package org.example.web_thue_gia_su_online.service.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseEnrollmentService {
    CourseEnrollmentResponse enrollCourse(CourseEnrollmentCreationRequest request);

    CourseEnrollmentResponse getEnrollment(String id);

    Page<CourseEnrollmentResponse> getEnrollmentsByStudent(Pageable pageable);

    Page<CourseEnrollmentResponse> getEnrollmentsByCourse(String courseId, Pageable pageable);

    CourseEnrollmentResponse updateEnrollment(String id);

    CourseEnrollmentResponse completeEnrollment(String id);

    void deleteEnrollment(String id);
}
