package org.example.web_thue_gia_su_online.repository.tutoring;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfile, String> {
    boolean existsByUserId(String userId);

    Optional<TutorProfile> findByUserId(String userId);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse(
			tp.user.id,
			tp.bio,
			tp.subjectsCanTeach,
			tp.rating,
			tp.createdAt
		)
		FROM TutorProfile tp
		WHERE tp.user.id = :userId
		""")
    Optional<TutorProfileResponse> findResponseByUserId(@Param("userId") String userId);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse(
			tp.user.id,
			tp.bio,
			tp.subjectsCanTeach,
			tp.rating,
			tp.createdAt
		)
		FROM TutorProfile tp
		""")
    Page<TutorProfileResponse> findResponses(Pageable pageable);
}
