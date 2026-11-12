package org.example.web_thue_gia_su_online.repository.identity_and_administration;

import java.time.Instant;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.ActionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, String> {

    @Query("""
		SELECT a
		FROM ActionLog a
		LEFT JOIN a.user u
		WHERE (CAST(:userId AS string) IS NULL OR u.id = :userId)
		AND (CAST(:action AS string) IS NULL OR STR(a.action) = :action)
		AND (CAST(:targetType AS string) IS NULL OR a.targetType = :targetType)
		AND (CAST(:fromDate AS java.time.Instant) IS NULL OR a.createdAt >= :fromDate)
		AND (CAST(:toDate AS java.time.Instant) IS NULL OR a.createdAt <= :toDate)
		ORDER BY a.createdAt DESC
		""")
    Page<ActionLog> searchActionLogs(
            @Param("userId") String userId,
            @Param("action") String action,
            @Param("targetType") String targetType,
            @Param("fromDate") Instant fromDate,
            @Param("toDate") Instant toDate,
            Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse(
			a.id,
			a.user.id,
			a.user.displayName,
			a.action,
			a.targetType,
			a.targetId,
			a.ipAddress,
			a.userAgent,
			a.createdAt
		)
		FROM ActionLog a
		WHERE a.user.id = :userId
		ORDER BY a.createdAt DESC
		""")
    Page<ActionLogResponse> findResponsesByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse(
			a.id,
			a.user.id,
			a.user.displayName,
			a.action,
			a.targetType,
			a.targetId,
			a.ipAddress,
			a.userAgent,
			a.createdAt
		)
		FROM ActionLog a
		LEFT JOIN a.user u
		WHERE (CAST(:userId AS string) IS NULL OR u.id = :userId)
		AND (CAST(:action AS string) IS NULL OR STR(a.action) = :action)
		AND (CAST(:targetType AS string) IS NULL OR a.targetType = :targetType)
		AND (CAST(:fromDate AS java.time.Instant) IS NULL OR a.createdAt >= :fromDate)
		AND (CAST(:toDate AS java.time.Instant) IS NULL OR a.createdAt <= :toDate)
		ORDER BY a.createdAt DESC
		""")
    Page<ActionLogResponse> searchActionLogsResponses(
            @Param("userId") String userId,
            @Param("action") String action,
            @Param("targetType") String targetType,
            @Param("fromDate") Instant fromDate,
            @Param("toDate") Instant toDate,
            Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse(
			a.id,
			a.user.id,
			a.user.displayName,
			a.action,
			a.targetType,
			a.targetId,
			a.ipAddress,
			a.userAgent,
			a.createdAt
		)
		FROM ActionLog a
		ORDER BY a.createdAt DESC
		""")
    Page<ActionLogResponse> findResponsesAll(Pageable pageable);
}
