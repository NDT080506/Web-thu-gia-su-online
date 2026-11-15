package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.ActionCode;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.CourseMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseService;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    CourseEnrollmentRepository courseEnrollmentRepository;
    UserRepository userRepository;
    CourseMapper courseMapper;
    ActionLogService actionLogService;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public CourseResponse create(CourseCreationRequest request) {
        String tutorId = getCurrentUserId();

        var tutor = userRepository.findById(tutorId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course = Course.builder()
                .tutor(tutor)
                .title(request.getTitle())
                .price(request.getPrice())
                .active(true)
                .createdAt(Instant.now())
                .build();

        Course savedCourse = courseRepository.save(course);
        actionLogService.log(ActionCode.CREATE_COURSE, tutor, "Course", savedCourse.getId());
        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getById(String id) {
        return courseRepository
                .findById(id)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> getCourses(Pageable pageable) {
        return courseRepository.findResponsesAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> getAllByTutorId(String tutorId, Pageable pageable) {
        return courseRepository.findResponsesByTutorId(tutorId, pageable);
    }

    @Override
    public CourseResponse update(String id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        String currentUserId = getCurrentUserId();
        if (course.getTutor() != null && !course.getTutor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không có quyền sửa khóa học này");
        }

        courseMapper.updateEntity(course, request);
        courseRepository.save(course);
        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public void delete(String id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        String currentUserId = getCurrentUserId();

        if (!course.getTutor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không có quyền xóa khóa học này");
        }

        if (courseEnrollmentRepository.existsByCourse_Id(id)) {
            course.setActive(false);
            courseRepository.save(course);
            return;
        }

        courseRepository.delete(course);
    }
}
