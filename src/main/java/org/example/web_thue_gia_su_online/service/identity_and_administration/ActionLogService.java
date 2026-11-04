package org.example.web_thue_gia_su_online.service.identity_and_administration;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.ActionCode;

public interface ActionLogService {
    void log(ActionCode actionCode, User user, String targetType, String targetId);

    List<ActionLogResponse> getActionLogs(
            String userId, String action, String targetType, Instant fromDate, Instant toDate);

    ActionLogResponse getActionLogById(String id);
}
