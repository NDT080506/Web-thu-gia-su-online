package org.example.web_thue_gia_su_online.service.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonService {
    LessonResponse createLesson(LessonCreationRequest request, String courseId);

    Page<LessonResponse> getLessonsByCourseId(String courseId, Pageable pageable);

    LessonResponse updateLesson(String id, LessonUpdateRequest request);

    void deleteLesson(String id);
}
