package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ReviewResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Page<Review> findAllByTutor_Id(String tutorId, Pageable pageable);

    Optional<Review> findByReviewer_Id(String reviewerId);

    boolean existsByBooking_Id(String bookingId);

    boolean existsByEnrollment_Id(String enrollmentId);

    @Query("""
		SELECT AVG(r.rating)
		FROM Review r
		WHERE r.tutor.id = :tutorId
	""")
    Double calculateAverageRating(@Param("tutorId") String tutorId);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.ReviewResponse(
			r.id,
			r.reviewer.id,
			r.tutor.id,
			r.booking.id,
			r.enrollment.id,
			r.rating,
			r.comment,
			r.createdAt,
			r.updatedAt
		)
		FROM Review r
		WHERE r.id = :id
		""")
    Optional<ReviewResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.ReviewResponse(
			r.id,
			r.reviewer.id,
			r.tutor.id,
			r.booking.id,
			r.enrollment.id,
			r.rating,
			r.comment,
			r.createdAt,
			r.updatedAt
		)
		FROM Review r
		WHERE r.tutor.id = :tutorId
		""")
    Page<ReviewResponse> findResponsesByTutorId(@Param("tutorId") String tutorId, Pageable pageable);
}
