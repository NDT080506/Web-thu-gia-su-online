package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findAllByTutor_Id(String tutorId);

    Optional<Review> findByReviewer_Id(String reviewerId);

    boolean existsByBooking_Id(String bookingId);

    boolean existsByEnrollment_Id(String enrollmentId);

    @Query("""
		SELECT AVG(r.rating)
		FROM Review r
		WHERE r.tutor.id = :tutorId
	""")
    Double calculateAverageRating(@Param("tutorId") String tutorId);
}
