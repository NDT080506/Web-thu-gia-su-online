package org.example.web_thue_gia_su_online.service.identity_and_administration.impl;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.ActionLogCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.ActionLog;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.ActionLogMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.ActionLogRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActionLogServiceImpl implements ActionLogService {

    ActionLogRepository actionLogRepository;
    UserRepository userRepository;
    ActionLogMapper actionLogMapper;

    @Override
    @Transactional
    public ActionLogResponse logAction(ActionLogCreationRequest request) {
        User admin = null;
        if (request.getAdminId() != null && !request.getAdminId().isBlank()) {
            admin = userRepository.findById(request.getAdminId()).orElse(null);
        }

        User targetUser = null;
        if (request.getTargetUserId() != null && !request.getTargetUserId().isBlank()) {
            targetUser = userRepository.findById(request.getTargetUserId()).orElse(null);
        }

        ActionLog actionLog = actionLogMapper.toEntity(request);
        actionLog.setAdmin(admin);
        actionLog.setTargetUser(targetUser);

        return actionLogMapper.toResponse(actionLogRepository.save(actionLog));
    }

    @Override
    public ActionLogResponse getLog(String id) {
        ActionLog logEntity =
                actionLogRepository.findById(id).orElseThrow(() -> new RuntimeException("Action log not found: " + id));
        return actionLogMapper.toResponse(logEntity);
    }

    @Override
    public List<ActionLogResponse> getLogsByTargetUser(String targetUserId) {
        return actionLogRepository.findByTargetUserId(targetUserId).stream()
                .map(actionLogMapper::toResponse)
                .toList();
    }

    @Override
    public List<ActionLogResponse> getLogsByAdmin(String adminId) {
        return actionLogRepository.findByAdminId(adminId).stream()
                .map(actionLogMapper::toResponse)
                .toList();
    }

    @Override
    public List<ActionLogResponse> getAllLogs() {
        return actionLogRepository.findAll().stream()
                .map(actionLogMapper::toResponse)
                .toList();
    }
}
