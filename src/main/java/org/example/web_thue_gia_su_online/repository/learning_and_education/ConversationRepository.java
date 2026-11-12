package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    Optional<Conversation> findByStudent_IdAndTutor_IdOrderByUpdatedAtDesc(String studentId, String tutorId);

    boolean existsByStudent_IdAndTutor_Id(String studentId, String tutorId);

    Page<Conversation> findAllByStudent_IdOrderByUpdatedAtDesc(String studentId, Pageable pageable);

    Page<Conversation> findAllByTutor_IdOrderByUpdatedAtDesc(String tutorId, Pageable pageable);

    Page<Conversation> findAllByStudent_IdOrTutor_IdOrderByUpdatedAtDesc(
            String studentId, String tutorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse(
			c.id,
			c.student.id,
			c.student.displayName,
			c.tutor.id,
			c.tutor.displayName,
			c.status,
			null,
			c.studentAgreedAt,
			c.createdAt,
			c.updatedAt
		)
		FROM Conversation c
		WHERE c.id = :id
		""")
    Optional<ConversationResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse(
			c.id,
			c.student.id,
			c.student.displayName,
			c.tutor.id,
			c.tutor.displayName,
			c.status,
			null,
			c.studentAgreedAt,
			c.createdAt,
			c.updatedAt
		)
		FROM Conversation c
		WHERE c.student.id = :studentId
		ORDER BY c.updatedAt DESC
		""")
    Page<ConversationResponse> findResponsesByStudentId(@Param("studentId") String studentId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse(
			c.id,
			c.student.id,
			c.student.displayName,
			c.tutor.id,
			c.tutor.displayName,
			c.status,
			null,
			c.studentAgreedAt,
			c.createdAt,
			c.updatedAt
		)
		FROM Conversation c
		WHERE c.tutor.id = :tutorId
		ORDER BY c.updatedAt DESC
		""")
    Page<ConversationResponse> findResponsesByTutorId(@Param("tutorId") String tutorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse(
			c.id,
			c.student.id,
			c.student.displayName,
			c.tutor.id,
			c.tutor.displayName,
			c.status,
			null,
			c.studentAgreedAt,
			c.createdAt,
			c.updatedAt
		)
		FROM Conversation c
		WHERE c.student.id = :userId OR c.tutor.id = :userId
		ORDER BY c.updatedAt DESC
		""")
    Page<ConversationResponse> findResponsesByStudentOrTutorId(@Param("userId") String userId, Pageable pageable);
}
