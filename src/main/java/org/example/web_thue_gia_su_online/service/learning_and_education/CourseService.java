package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;

public interface CourseService {

    CourseResponse create(CourseCreationRequest request);

    CourseResponse getById(String id);

    List<CourseResponse> getByTutorId(String tutorId);
}
