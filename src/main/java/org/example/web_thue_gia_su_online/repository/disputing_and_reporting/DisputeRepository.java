package org.example.web_thue_gia_su_online.repository.disputing_and_reporting;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.enums.disputing_and_reporting.DisputeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, String> {
    Page<Dispute> findByCreatedBy_Id(String userId, Pageable pageable);

    Optional<Dispute> findByIdAndCreatedBy_Id(String id, String userId);

    boolean existsByBooking_IdAndStatus(String bookingId, DisputeStatus statuses);

    boolean existsByEnrollment_IdAndStatus(String enrollmentId, DisputeStatus statuses);

    Page<Dispute> findByStatus(DisputeStatus status, Pageable pageable);

    boolean existsByIdAndStatus(String id, DisputeStatus status);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse(
			d.id,
			d.booking.id,
			d.enrollment.id,
			d.createdBy.id,
			d.reason,
			d.evidenceUrl,
			d.status,
			d.refundAmount,
			d.adminNote,
			d.createdAt,
			d.resolvedAt
		)
		FROM Dispute d
		WHERE d.id = :id
		""")
    Optional<DisputeResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse(
			d.id,
			d.booking.id,
			d.enrollment.id,
			d.createdBy.id,
			d.reason,
			d.evidenceUrl,
			d.status,
			d.refundAmount,
			d.adminNote,
			d.createdAt,
			d.resolvedAt
		)
		FROM Dispute d
		WHERE d.createdBy.id = :userId
		ORDER BY d.createdAt DESC
		""")
    Page<DisputeResponse> findResponsesByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse(
			d.id,
			d.booking.id,
			d.enrollment.id,
			d.createdBy.id,
			d.reason,
			d.evidenceUrl,
			d.status,
			d.refundAmount,
			d.adminNote,
			d.createdAt,
			d.resolvedAt
		)
		FROM Dispute d
		ORDER BY d.createdAt DESC
		""")
    Page<DisputeResponse> findResponsesAll(Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse(
			d.id,
			d.booking.id,
			d.enrollment.id,
			d.createdBy.id,
			d.reason,
			d.evidenceUrl,
			d.status,
			d.refundAmount,
			d.adminNote,
			d.createdAt,
			d.resolvedAt
		)
		FROM Dispute d
		WHERE d.status = :status
		ORDER BY d.createdAt DESC
		""")
    Page<DisputeResponse> findResponsesByStatus(@Param("status") DisputeStatus status, Pageable pageable);
}
