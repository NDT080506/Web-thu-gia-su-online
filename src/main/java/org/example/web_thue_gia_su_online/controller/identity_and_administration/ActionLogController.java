package org.example.web_thue_gia_su_online.controller.identity_and_administration;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.ActionLogCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/action-logs")
public class ActionLogController {

    ActionLogService actionLogService;

    @PostMapping
    ApiResponse<ActionLogResponse> logAction(@RequestBody @Valid ActionLogCreationRequest request) {
        return ApiResponse.<ActionLogResponse>builder()
                .result(actionLogService.logAction(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ActionLogResponse> getLog(@PathVariable String id) {
        return ApiResponse.<ActionLogResponse>builder()
                .result(actionLogService.getLog(id))
                .build();
    }

    @GetMapping("/target-user/{targetUserId}")
    ApiResponse<List<ActionLogResponse>> getLogsByTargetUser(@PathVariable String targetUserId) {
        return ApiResponse.<List<ActionLogResponse>>builder()
                .result(actionLogService.getLogsByTargetUser(targetUserId))
                .build();
    }

    @GetMapping("/admin/{adminId}")
    ApiResponse<List<ActionLogResponse>> getLogsByAdmin(@PathVariable String adminId) {
        return ApiResponse.<List<ActionLogResponse>>builder()
                .result(actionLogService.getLogsByAdmin(adminId))
                .build();
    }

    @GetMapping
    ApiResponse<List<ActionLogResponse>> getAllLogs() {
        return ApiResponse.<List<ActionLogResponse>>builder()
                .result(actionLogService.getAllLogs())
                .build();
    }
}
