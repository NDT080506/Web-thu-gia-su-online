package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Lesson;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.ActionCode;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.LessonMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.LessonRepository;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.example.web_thue_gia_su_online.service.learning_and_education.LessonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    ActionLogService actionLogService;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    private Course verifyCourseOwnerAndGet(String courseId) {
        String currentUserId = getCurrentUserId();
        Course course =
                courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // ADMIN được phép, TUTOR phải là chủ sở hữu
        if (!course.getTutor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Chỉ gia sư sở hữu khóa học mới được thao tác");
        }
        return course;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public LessonResponse createLesson(LessonCreationRequest request, String courseId) {
        Course course =
                courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        // Kiểm tra ownership — chỉ tutor sở hữu course mới được tạo lesson
        String currentUserId = getCurrentUserId();
        if (!course.getTutor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Chỉ gia sư sở hữu khóa học mới được tạo bài học");
        }

        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCourse(course);
        lesson.setCreatedAt(Instant.now());

        Lesson savedLesson = lessonRepository.save(lesson);
        actionLogService.log(ActionCode.CREATE_LESSON, course.getTutor(), "Lesson", savedLesson.getId());
        return lessonMapper.toResponse(savedLesson);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LessonResponse> getLessonsByCourseId(String courseId, Pageable pageable) {
        return lessonRepository.findResponsesByCourseId(courseId, pageable);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public LessonResponse updateLesson(String id, LessonUpdateRequest request) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        // Kiểm tra ownership
        Course course = verifyCourseOwnerAndGet(lesson.getCourse().getId());

        lessonMapper.updateEntity(lesson, request);
        lessonRepository.save(lesson);
        actionLogService.log(ActionCode.UPDATE_LESSON, course.getTutor(), "Lesson", id);
        return lessonMapper.toResponse(lesson);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public void deleteLesson(String id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        // Kiểm tra ownership
        Course course = verifyCourseOwnerAndGet(lesson.getCourse().getId());

        lessonRepository.delete(lesson);
        actionLogService.log(ActionCode.DELETE_LESSON, course.getTutor(), "Lesson", id);
    }
}
