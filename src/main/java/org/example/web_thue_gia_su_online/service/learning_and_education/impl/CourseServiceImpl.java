package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.CourseMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    UserRepository userRepository;
    CourseMapper courseMapper;

    @Override
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN')")
    public CourseResponse create(CourseCreationRequest request) {
        String tutorId = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        var tutor = userRepository
                .findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor not found"));

        Course course = Course.builder()
                .tutor(tutor)
                .title(request.getTitle())
                .price(request.getPrice())
                .isLive(request.getIsLive() != null && request.getIsLive())
                .isActive(request.getIsActive() != null && request.getIsActive())
                .createdAt(Instant.now())
                .build();

        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN', 'STUDENT')")
    public CourseResponse getById(String id) {
        return courseRepository
                .findById(id)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Override
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN', 'STUDENT')")
    public List<CourseResponse> getCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponse)
                .toList();
    }


    @Override
    @PreAuthorize("hasAnyRole('TUTOR', 'ADMIN', 'STUDENT')")
    public List<CourseResponse> getAllByTutorId(String tutorId) {
        return courseRepository.findAllByTutorId(tutorId).stream()
                .map(courseMapper::toResponse)
                .toList();
    }
}
