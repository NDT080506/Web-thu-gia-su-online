package org.example.web_thue_gia_su_online.repository.disputing_and_reporting;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, String> {
    Optional<Dispute> findByBookingId(String bookingId);

    List<Dispute> findByStudentId(String studentId);
}
