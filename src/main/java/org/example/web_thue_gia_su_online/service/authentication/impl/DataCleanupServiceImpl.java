package org.example.web_thue_gia_su_online.service.authentication.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.example.web_thue_gia_su_online.repository.authentication.InvalidatedTokenRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.ActionLogRepository;
import org.example.web_thue_gia_su_online.service.authentication.DataCleanupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataCleanupServiceImpl implements DataCleanupService {
    InvalidatedTokenRepository invalidatedTokenRepository;
    ActionLogRepository actionLogRepository;

    @Override
    @Transactional
    public int cleanupExpiredTokens() {
        int deleted = invalidatedTokenRepository.deleteExpiredTokens(new Date());

        log.info("Cleaned up {} expired invalidated tokens", deleted);

        return deleted;
    }

    @Override
    @Transactional
    public int cleanupOldActionLogs(int retentionDays) {
        Instant cutoff = Instant.now().minus(retentionDays, ChronoUnit.DAYS);

        int deleted = actionLogRepository.deleteOlderThan(cutoff);

        log.info("Cleaned up {} old action logs", deleted);

        return deleted;
    }
}
