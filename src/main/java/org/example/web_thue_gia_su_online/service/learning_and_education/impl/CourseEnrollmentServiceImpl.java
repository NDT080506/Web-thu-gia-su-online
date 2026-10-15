package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.CourseEnrollmentMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseEnrollmentService;
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
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    CourseEnrollmentRepository courseEnrollmentRepository;
    UserRepository userRepository;
    CourseRepository courseRepository;
    CourseEnrollmentMapper courseEnrollmentMapper;

    @Override
    @Transactional
    public CourseEnrollmentResponse enrollCourse(CourseEnrollmentCreationRequest request) {
        User student = userRepository
                .findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + request.getStudentId()));

        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + request.getCourseId()));

        courseEnrollmentRepository
                .findByStudentIdAndCourseId(student.getId(), course.getId())
                .ifPresent(existing -> {
                    throw new RuntimeException("Student is already enrolled in this course");
                });

        Instant now = Instant.now();
        CourseEnrollment enrollment = CourseEnrollment.builder()
                .student(student)
                .course(course)
                .pricePaid(request.getPricePaid())
                .status("active")
                .progressPercent(0)
                .createdAt(now)
                .build();

        return courseEnrollmentMapper.toResponse(courseEnrollmentRepository.save(enrollment));
    }

    @Override
    public CourseEnrollmentResponse getEnrollment(String id) {
        CourseEnrollment enrollment = courseEnrollmentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found: " + id));
        return courseEnrollmentMapper.toResponse(enrollment);
    }

    @Override
    public List<CourseEnrollmentResponse> getEnrollmentsByStudent(String studentId) {
        return courseEnrollmentRepository.findByStudentId(studentId).stream()
                .map(courseEnrollmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<CourseEnrollmentResponse> getEnrollmentsByCourse(String courseId) {
        return courseEnrollmentRepository.findByCourseId(courseId).stream()
                .map(courseEnrollmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CourseEnrollmentResponse updateEnrollment(String id, CourseEnrollmentUpdateRequest request) {
        CourseEnrollment enrollment = courseEnrollmentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found: " + id));

        courseEnrollmentMapper.updateEntity(enrollment, request);
        if (enrollment.getProgressPercent() >= 100 && enrollment.getCompletedAt() == null) {
            enrollment.setCompletedAt(Instant.now());
            enrollment.setStatus("completed");
        }

        return courseEnrollmentMapper.toResponse(courseEnrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional
    public void cancelEnrollment(String id) {
        CourseEnrollment enrollment = courseEnrollmentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found: " + id));
        courseEnrollmentRepository.delete(enrollment);
    }
}
