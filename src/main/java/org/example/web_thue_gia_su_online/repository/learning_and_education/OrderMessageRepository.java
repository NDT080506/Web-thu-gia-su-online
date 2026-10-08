package org.example.web_thue_gia_su_online.repository.learning_and_education;

import org.example.web_thue_gia_su_online.entity.learning_and_education.OrderMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMessageRepository extends JpaRepository<OrderMessage, String> {}
