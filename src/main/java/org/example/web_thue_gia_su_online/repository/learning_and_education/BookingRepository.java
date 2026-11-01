package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
		SELECT b
		FROM Booking b
		WHERE b.id = :bookingId
		""")
    Optional<Booking> findByIdForUpdate(@Param("bookingId") String bookingId);

    List<Booking> findAllByStudentId(String studentId);

    List<Booking> findAllByTutorId(String tutorId);
}
