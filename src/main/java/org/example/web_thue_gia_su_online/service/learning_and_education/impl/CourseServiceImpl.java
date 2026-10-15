package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.CourseMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseService;
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
    public CourseResponse create(CourseCreationRequest request) {
        var tutor = userRepository
                .findById(request.getTutorId())
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
    public CourseResponse getById(String id) {
        return courseRepository
                .findById(id)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    @Override
    public List<CourseResponse> getByTutorId(String tutorId) {
        return courseRepository.findByTutorId(tutorId).stream()
                .map(courseMapper::toResponse)
                .toList();
    }
}
