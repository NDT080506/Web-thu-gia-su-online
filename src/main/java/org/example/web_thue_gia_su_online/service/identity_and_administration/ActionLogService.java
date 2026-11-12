package org.example.web_thue_gia_su_online.service.identity_and_administration;

import java.time.Instant;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.ActionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActionLogService {
    void log(ActionCode actionCode, User user, String targetType, String targetId);

    Page<ActionLogResponse> getActionLogs(
            String userId, String action, String targetType, Instant fromDate, Instant toDate, Pageable pageable);

    ActionLogResponse getActionLogById(String id);
}
