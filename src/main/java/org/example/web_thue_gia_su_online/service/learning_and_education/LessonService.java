package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;

public interface LessonService {
    LessonResponse createLesson(LessonCreationRequest request);

    LessonResponse getLesson(String id);

    List<LessonResponse> getLessonsByCourseId(String courseId);

    LessonResponse updateLesson(String id, LessonUpdateRequest request);

    void deleteLesson(String id);
}
