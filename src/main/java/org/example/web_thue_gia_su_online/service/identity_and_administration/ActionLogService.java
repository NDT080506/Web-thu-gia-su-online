package org.example.web_thue_gia_su_online.service.identity_and_administration;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.ActionLogCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;

public interface ActionLogService {
    ActionLogResponse logAction(ActionLogCreationRequest request);

    ActionLogResponse getLog(String id);

    List<ActionLogResponse> getLogsByTargetUser(String targetUserId);

    List<ActionLogResponse> getLogsByAdmin(String adminId);

    List<ActionLogResponse> getAllLogs();
}
