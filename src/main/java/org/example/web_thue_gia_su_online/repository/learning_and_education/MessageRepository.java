package org.example.web_thue_gia_su_online.repository.learning_and_education;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    Page<Message> findAllByConversation_IdOrderByCreatedAtAsc(String conversationId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse(
			m.id,
			m.conversation.id,
			m.sender.id,
			m.sender.displayName,
			m.content,
			m.createdAt
		)
		FROM Message m
		WHERE m.conversation.id = :conversationId
		ORDER BY m.createdAt ASC
		""")
    Page<MessageResponse> findResponsesByConversationId(
            @Param("conversationId") String conversationId, Pageable pageable);
}
