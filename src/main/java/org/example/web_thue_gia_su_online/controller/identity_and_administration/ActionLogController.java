package org.example.web_thue_gia_su_online.controller.identity_and_administration;

import java.time.Instant;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin/action-logs")
public class ActionLogController {

    ActionLogService actionLogService;

    @GetMapping
    public Page<ActionLogResponse> getActionLogs(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
            Pageable pageable) {

        return actionLogService.getActionLogs(userId, action, targetType, fromDate, toDate, pageable);
    }

    @GetMapping("/{id}")
    public ActionLogResponse getActionLogById(@PathVariable String id) {

        return actionLogService.getActionLogById(id);
    }
}
