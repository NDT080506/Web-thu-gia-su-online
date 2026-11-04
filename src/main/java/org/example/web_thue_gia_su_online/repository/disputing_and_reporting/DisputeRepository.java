package org.example.web_thue_gia_su_online.repository.disputing_and_reporting;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, String> {
    List<Dispute> findByCreatedBy_Id(String userId);

    Optional<Dispute> findByIdAndCreatedBy_Id(String id, String userId);

    boolean existsByBooking_IdAndStatus(String bookingId, DisputeStatus statuses);

    boolean existsByEnrollment_IdAndStatus(String enrollmentId, DisputeStatus statuses);

    List<Dispute> findByStatus(DisputeStatus status);

    boolean existsSuccessfulRefund(String disputeId, DisputeStatus status);
}
