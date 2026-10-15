package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Lesson;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.LessonMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.LessonRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.LessonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LessonServiceImpl implements LessonService {

    LessonRepository lessonRepository;
    CourseRepository courseRepository;
    LessonMapper lessonMapper;

    @Override
    @Transactional
    public LessonResponse createLesson(LessonCreationRequest request) {
        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + request.getCourseId()));

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);
        lesson.setCreatedAt(Instant.now());

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    public LessonResponse getLesson(String id) {
        Lesson lesson =
                lessonRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found: " + id));
        return lessonMapper.toResponse(lesson);
    }

    @Override
    public List<LessonResponse> getLessonsByCourseId(String courseId) {
        return lessonRepository.findByCourseId(courseId).stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(String id, LessonUpdateRequest request) {
        Lesson lesson =
                lessonRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found: " + id));

        lessonMapper.updateEntity(lesson, request);
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public void deleteLesson(String id) {
        Lesson lesson =
                lessonRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found: " + id));
        lessonRepository.delete(lesson);
    }
}
