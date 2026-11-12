package org.example.web_thue_gia_su_online.repository.tutoring;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.enums.tutoring.ApplicationRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorApplicationRepository extends JpaRepository<TutorApplication, String> {
    Optional<TutorApplication> findByUserId(String userId);

    boolean existsByUserIdAndStatus(String userId, ApplicationRequestStatus status);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse(
			a.id,
			a.user.id,
			a.user.displayName,
			a.user.email,
			a.bio,
			a.subjectsCanTeach,
			a.cvUrl,
			a.testSubmission,
			a.status,
			a.adminNote,
			a.createdAt
		)
		FROM TutorApplication a
		WHERE a.id = :id
		""")
    Optional<TutorApplicationResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse(
			a.id,
			a.user.id,
			a.user.displayName,
			a.user.email,
			a.bio,
			a.subjectsCanTeach,
			a.cvUrl,
			a.testSubmission,
			a.status,
			a.adminNote,
			a.createdAt
		)
		FROM TutorApplication a
		WHERE a.user.id = :userId
		""")
    Optional<TutorApplicationResponse> findResponseByUserId(@Param("userId") String userId);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse(
			a.id,
			a.user.id,
			a.user.displayName,
			a.user.email,
			a.bio,
			a.subjectsCanTeach,
			a.cvUrl,
			a.testSubmission,
			a.status,
			a.adminNote,
			a.createdAt
		)
		FROM TutorApplication a
		""")
    Page<TutorApplicationResponse> findResponses(Pageable pageable);
}
