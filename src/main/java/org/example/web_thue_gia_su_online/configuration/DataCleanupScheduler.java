package org.example.web_thue_gia_su_online.configuration;

import org.example.web_thue_gia_su_online.service.authentication.DataCleanupService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataCleanupScheduler {
    private final DataCleanupService dataCleanupService;

    @Value("${app.cleanup.action-log-retention-days:365}")
    private int actionLogRetentionDays;

    @Scheduled(cron = "${app.cleanup.cron:0 0 3 * * *}", zone = "${app.cleanup.zone:Asia/Ho_Chi_Minh}")
    public void cleanup() {

        log.info("Starting scheduled data cleanup...");

        try {
            dataCleanupService.cleanupExpiredTokens();
        } catch (Exception e) {
            log.error("Failed to cleanup invalidated tokens", e);
        }

        try {
            dataCleanupService.cleanupOldActionLogs(actionLogRetentionDays);
        } catch (Exception e) {
            log.error("Failed to cleanup old action logs", e);
        }

        log.info("Scheduled data cleanup completed.");
    }
}
