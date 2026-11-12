package org.example.web_thue_gia_su_online.service.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    CourseResponse create(CourseCreationRequest request);

    CourseResponse getById(String id);

    Page<CourseResponse> getCourses(Pageable pageable);

    Page<CourseResponse> getAllByTutorId(String tutorId, Pageable pageable);
}
