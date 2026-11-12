package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Booking> findAllByStudentId(String studentId, Pageable pageable);

    //    Page<Booking> findAllByTutorId(String tutorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse(
			b.id,
			b.brokeragePackage.id,
			b.brokeragePackage.name,
			b.student.id,
			b.student.displayName,
			b.conversation.tutor.id,
			b.status,
			b.totalPrice,
			b.platformFee,
			b.tutorIncome,
			b.createdAt,
			b.updatedAt
		)
		FROM Booking b
		WHERE b.id = :id
		""")
    Optional<BookingResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse(
			b.id,
			b.brokeragePackage.id,
			b.brokeragePackage.name,
			b.student.id,
			b.student.displayName,
			b.conversation.tutor.id,
			b.status,
			b.totalPrice,
			b.platformFee,
			b.tutorIncome,
			b.createdAt,
			b.updatedAt
		)
		FROM Booking b
		WHERE b.student.id = :studentId
		""")
    Page<BookingResponse> findResponsesByStudentId(@Param("studentId") String studentId, Pageable pageable);
}
