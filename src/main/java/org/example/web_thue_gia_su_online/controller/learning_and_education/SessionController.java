package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.SessionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.SessionResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.SessionService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/sessions")
public class SessionController {

    SessionService sessionService;

    @PostMapping("/{orderId}")
    ApiResponse<SessionResponse> startSession(
            @RequestBody @Valid SessionCreationRequest request, @PathVariable String orderId) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.startSession(request, orderId))
                .build();
    }

    @PutMapping("/{id}/end")
    ApiResponse<SessionResponse> endSession(@PathVariable String id) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.endSession(id))
                .build();
    }

    @GetMapping
    ApiResponse<List<SessionResponse>> getSessions() {
        return ApiResponse.<List<SessionResponse>>builder()
                .result(sessionService.getSessions())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<SessionResponse> getSession(@PathVariable String id) {
        return ApiResponse.<SessionResponse>builder()
                .result(sessionService.getSession(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteSession(@PathVariable String id) {
        sessionService.deleteSession(id);

        return ApiResponse.<Void>builder().message("Session has been deleted").build();
    }
}
