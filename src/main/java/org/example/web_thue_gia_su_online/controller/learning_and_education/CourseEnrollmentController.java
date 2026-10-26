package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseEnrollmentService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/course-enrollments")
public class CourseEnrollmentController {

    CourseEnrollmentService courseEnrollmentService;

    @PostMapping
    ApiResponse<CourseEnrollmentResponse> enrollCourse(@RequestBody @Valid CourseEnrollmentCreationRequest request) {
        return ApiResponse.<CourseEnrollmentResponse>builder()
                .result(courseEnrollmentService.enrollCourse(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<CourseEnrollmentResponse> getEnrollment(@PathVariable String id) {
        return ApiResponse.<CourseEnrollmentResponse>builder()
                .result(courseEnrollmentService.getEnrollment(id))
                .build();
    }

    @GetMapping("/me")
    ApiResponse<List<CourseEnrollmentResponse>> getEnrollmentsByStudent() {
        return ApiResponse.<List<CourseEnrollmentResponse>>builder()
                .result(courseEnrollmentService.getEnrollmentsByStudent())
                .build();
    }

    @GetMapping("/course/{courseId}")
    ApiResponse<List<CourseEnrollmentResponse>> getEnrollmentsByCourse(@PathVariable String courseId) {
        return ApiResponse.<List<CourseEnrollmentResponse>>builder()
                .result(courseEnrollmentService.getEnrollmentsByCourse(courseId))
                .build();
    }

    @PatchMapping("/{id}/cancel")
    ApiResponse<CourseEnrollmentResponse> cancelEnrollment(@PathVariable String id) {
        return ApiResponse.<CourseEnrollmentResponse>builder()
                .result(courseEnrollmentService.updateEnrollment(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteEnrollment(@PathVariable String id) {
        courseEnrollmentService.deleteEnrollment(id);
        return ApiResponse.<Void>builder()
                .message("Enrollment deleted successfully")
                .build();
    }
}
