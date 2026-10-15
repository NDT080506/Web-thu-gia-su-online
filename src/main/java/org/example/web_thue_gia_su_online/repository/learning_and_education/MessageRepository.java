package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByBookingIdOrderByCreatedAtAsc(String bookingId);
}
