package org.example.web_thue_gia_su_online.controller.tutoring;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorProfileUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse;
import org.example.web_thue_gia_su_online.service.tutoring.TutorProfileService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/tutor-profiles")
public class TutorProfileController {

    TutorProfileService tutorProfileService;

//    @PostMapping("/{userid}")
//    ApiResponse<TutorProfileResponse> createTutorProfile(
//            @RequestBody @Valid TutorProfileCreationRequest request, @PathVariable String userid) {
//        return ApiResponse.<TutorProfileResponse>builder()
//                .result(tutorProfileService.createTutorProfile(request, userid))
//                .build();
//    }

    @GetMapping
    ApiResponse<TutorProfileResponse> getMyTutorProfile() {
        return ApiResponse.<TutorProfileResponse>builder()
                .result(tutorProfileService.getMyTutorProfile())
                .build();
    }

    @GetMapping
    ApiResponse<List<TutorProfileResponse>> getTutorProfiles() {
        return ApiResponse.<List<TutorProfileResponse>>builder()
                .result(tutorProfileService.getTutorProfiles())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<TutorProfileResponse> getTutorProfile(@PathVariable String userId) {
        return ApiResponse.<TutorProfileResponse>builder()
                .result(tutorProfileService.getTutorProfile(userId))
                .build();
    }

    @PatchMapping
    ApiResponse<TutorProfileResponse> updateMyTutorProfile(
            @RequestBody @Valid TutorProfileUpdateRequest request) {
        return ApiResponse.<TutorProfileResponse>builder()
                .result(tutorProfileService.updateMyTutorProfile(request))
                .build();
    }

    @PatchMapping("/{userId}")
    ApiResponse<TutorProfileResponse> updateTutorProfile(
            @RequestBody @Valid TutorProfileUpdateRequest request, @PathVariable String userId) {
        return ApiResponse.<TutorProfileResponse>builder()
                .result(tutorProfileService.updateTutorProfile(request, userId))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Void> deleteTutorProfile(@PathVariable String userId) {
        tutorProfileService.deleteTutorProfile(userId);

        return ApiResponse.<Void>builder()
                .message("Tutor profile has been deleted")
                .build();
    }
}
