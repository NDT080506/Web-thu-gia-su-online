package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.LessonService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/lessons")
public class LessonController {

    LessonService lessonService;

    @PostMapping("/courses/{courseId}")
    ApiResponse<LessonResponse> createLesson(@RequestBody @Valid LessonCreationRequest request, @PathVariable String courseId) {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.createLesson(request, courseId))
                .build();
    }

    @GetMapping("/course/{courseId}")
    ApiResponse<List<LessonResponse>> getLessonsByCourseId(@PathVariable String courseId) {
        return ApiResponse.<List<LessonResponse>>builder()
                .result(lessonService.getLessonsByCourseId(courseId))
                .build();
    }

    @PatchMapping("/{id}")
    ApiResponse<LessonResponse> updateLesson(@PathVariable String id, @RequestBody @Valid LessonUpdateRequest request) {
        return ApiResponse.<LessonResponse>builder()
                .result(lessonService.updateLesson(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteLesson(@PathVariable String id) {
        lessonService.deleteLesson(id);
        return ApiResponse.<Void>builder()
                .message("Lesson deleted successfully")
                .build();
    }
}
