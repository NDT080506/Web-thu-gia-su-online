package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseController {

    CourseService courseService;

    @PostMapping
    public ApiResponse<CourseResponse> create(@RequestBody @Valid CourseCreationRequest request) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.create(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseResponse> getById(@PathVariable String id) {
        return ApiResponse.<CourseResponse>builder()
                .result(courseService.getById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CourseResponse>> getCourses() {
        return ApiResponse.<List<CourseResponse>>builder()
                .result(courseService.getCourses())
                .build();
    }

    @GetMapping("/tutor/{tutorId}")
    public ApiResponse<List<CourseResponse>> getAllByTutorId(@PathVariable String tutorId) {
        return ApiResponse.<List<CourseResponse>>builder()
                .result(courseService.getAllByTutorId(tutorId))
                .build();
    }
}
