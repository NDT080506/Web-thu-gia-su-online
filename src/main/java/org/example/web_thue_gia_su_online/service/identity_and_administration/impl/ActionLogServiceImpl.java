package org.example.web_thue_gia_su_online.service.identity_and_administration.impl;

import java.time.Instant;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.ActionLog;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.ActionCode;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.ActionLogMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.ActionLogRepository;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActionLogServiceImpl implements ActionLogService {

    ActionLogRepository actionLogRepository;
    ActionLogMapper actionLogMapper;

    @Override
    @Transactional
    public void log(ActionCode actionCode, User user, String targetType, String targetId) {

        HttpServletRequest request = getCurrentRequest();

        ActionLog actionLog = ActionLog.builder()
                .user(user)
                .action(actionCode)
                .targetType(targetType)
                .targetId(targetId)
                .ipAddress(request != null ? request.getRemoteAddr() : null)
                .userAgent(request != null ? request.getHeader("User-Agent") : null)
                .build();

        actionLogRepository.save(actionLog);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<ActionLogResponse> getActionLogs(
            String userId, String action, String targetType, Instant fromDate, Instant toDate) {

        if (userId == null && action == null && targetType == null && fromDate == null && toDate == null) {
            return actionLogRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                    .map(actionLogMapper::toResponse)
                    .toList();
        }

        return actionLogRepository.searchActionLogs(userId, action, targetType, fromDate, toDate).stream()
                .map(actionLogMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public ActionLogResponse getActionLogById(String id) {

        ActionLog actionLog = actionLogRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Action log not found"));

        return actionLogMapper.toResponse(actionLog);
    }

    private HttpServletRequest getCurrentRequest() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return attributes != null ? attributes.getRequest() : null;
    }
}
