package org.example.web_thue_gia_su_online.service.authentication;

public interface DataCleanupService {
    public int cleanupExpiredTokens();

    public int cleanupOldActionLogs(int retentionDays);
}
