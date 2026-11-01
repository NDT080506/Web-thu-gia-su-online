package org.example.web_thue_gia_su_online.repository.identity_and_administration;

import java.util.List;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, String> {
    List<ActionLog> findByTargetUserId(String targetUserId);

    List<ActionLog> findByAdminId(String adminId);
}
