package org.example.web_thue_gia_su_online.controller.tutoring;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.service.tutoring.TutorApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/tutorapplications")
public class TutorApplicationController {

    TutorApplicationService tutorApplicationService;

    @PostMapping("/{userId}")
    ApiResponse<TutorApplicationResponse> createTutorApplication(
            @RequestBody @Valid TutorApplicationCreationRequest request, @PathVariable String userId) {
        return ApiResponse.<TutorApplicationResponse>builder()
                .result(tutorApplicationService.createTutorApplication(request, userId))
                .build();
    }

    @GetMapping
    ApiResponse<List<TutorApplicationResponse>> getTutorApplications() {
        return ApiResponse.<List<TutorApplicationResponse>>builder()
                .result(tutorApplicationService.getTutorApplications())
                .build();
    }

    @GetMapping("/{applicationId}")
    ApiResponse<TutorApplicationResponse> getTutorApplication(@PathVariable String applicationId) {
        return ApiResponse.<TutorApplicationResponse>builder()
                .result(tutorApplicationService.getTutorApplication(applicationId))
                .build();
    }

    @DeleteMapping("/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<Void> deleteTutorApplication(@PathVariable String applicationId) {
        tutorApplicationService.deleteTutorApplication(applicationId);

        return ApiResponse.<Void>builder()
                .message("Tutor application has been deleted")
                .build();
    }
}
