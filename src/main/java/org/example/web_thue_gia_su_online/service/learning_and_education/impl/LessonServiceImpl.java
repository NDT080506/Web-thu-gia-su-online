package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Lesson;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.LessonMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.LessonRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.LessonService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    private void verifyCourseOwner(String courseId) {
        String currentUserId = getCurrentUserId();
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // ADMIN được phép, TUTOR phải là chủ sở hữu
        if (!course.getTutor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Chỉ gia sư sở hữu khóa học mới được thao tác");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public LessonResponse createLesson(LessonCreationRequest request, String courseId) {
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // Kiểm tra ownership — chỉ tutor sở hữu course mới được tạo lesson
        String currentUserId = getCurrentUserId();
        if (!course.getTutor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Chỉ gia sư sở hữu khóa học mới được tạo bài học");
        }

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);
        lesson.setCreatedAt(Instant.now());

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN', 'STUDENT')")
    public List<LessonResponse> getLessonsByCourseId(String courseId) {
        return lessonRepository.findByCourseId(courseId).stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public LessonResponse updateLesson(String id, LessonUpdateRequest request) {
        Lesson lesson = lessonRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        // Kiểm tra ownership
        verifyCourseOwner(lesson.getCourse().getId());

        lessonMapper.updateEntity(lesson, request);
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public void deleteLesson(String id) {
        Lesson lesson = lessonRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        // Kiểm tra ownership
        verifyCourseOwner(lesson.getCourse().getId());

        lessonRepository.delete(lesson);
    }
}
