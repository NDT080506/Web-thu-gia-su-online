package org.example.web_thue_gia_su_online.controller.tutoring;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.service.tutoring.TutorApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/tutor-applications")
public class TutorApplicationController {

    TutorApplicationService tutorApplicationService;

    @PostMapping
    ApiResponse<TutorApplicationResponse> createTutorApplication(
            @RequestBody @Valid TutorApplicationCreationRequest request) {
        return ApiResponse.<TutorApplicationResponse>builder()
                .result(tutorApplicationService.createTutorApplication(request))
                .build();
    }

    @GetMapping
    ApiResponse<TutorApplicationResponse> getMyTutorApplication() {
        return ApiResponse.<TutorApplicationResponse>builder()
                .result(tutorApplicationService.getMyTutorApplication())
                .build();
    }

    @DeleteMapping
    ApiResponse<Void> deleteMyTutorApplication() {
        tutorApplicationService.deleteMyTutorApplication();

        return ApiResponse.<Void>builder()
                .message("Tutor application has been deleted")
                .build();
    }

    @GetMapping("/admin")
    ApiResponse<List<TutorApplicationResponse>> getTutorApplications() {
        return ApiResponse.<List<TutorApplicationResponse>>builder()
                .result(tutorApplicationService.getTutorApplications())
                .build();
    }

    @GetMapping("/admin/{applicationId}")
    ApiResponse<TutorApplicationResponse> getTutorApplication(@PathVariable String applicationId) {
        return ApiResponse.<TutorApplicationResponse>builder()
                .result(tutorApplicationService.getTutorApplication(applicationId))
                .build();
    }

    @PatchMapping("/admin/{applicationId}/approve")
    ApiResponse<TutorApplicationResponse> approveTutorApplication(@PathVariable String applicationId){
        return ApiResponse.<TutorApplicationResponse>builder()
                .result(tutorApplicationService.approveTutorApplication(applicationId))
                .build();
    }

    @PatchMapping("/admin/{applicationId}/reject")
    ApiResponse<TutorApplicationResponse> rejectTutorApplication(@PathVariable String applicationId){
        return ApiResponse.<TutorApplicationResponse>builder()
                .result(tutorApplicationService.rejectTutorApplication(applicationId))
                .build();
    }

    @DeleteMapping("/admin/{applicationId}")
    ApiResponse<Void> deleteTutorApplication(@PathVariable String applicationId) {
        tutorApplicationService.deleteTutorApplication(applicationId);

        return ApiResponse.<Void>builder()
                .message("Tutor application has been deleted")
                .build();
    }
}
