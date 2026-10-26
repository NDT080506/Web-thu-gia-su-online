package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {
    Optional<Conversation> findByStudent_IdAndTutor_IdOrderByUpdatedAtDesc(String studentId, String tutorId);

    boolean existsByStudent_IdAndTutor_Id(String studentId, String tutorId);

    List<Conversation> findAllByStudent_IdOrderByUpdatedAtDesc(String studentId);

    List<Conversation> findAllByTutor_IdOrderByUpdatedAtDesc(String tutorId);

    List<Conversation> findAllByStudent_IdOrTutor_IdOrderByUpdatedAtDesc(String studentId, String tutorId);
}
