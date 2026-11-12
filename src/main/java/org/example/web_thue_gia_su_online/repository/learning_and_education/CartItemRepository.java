package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.learning_and_education.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    Page<CartItem> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    Optional<CartItem> findByUserIdAndCourseId(String userId, String courseId);

    boolean existsByUserIdAndCourseId(String userId, String courseId);

    void deleteByUserIdAndCourseId(String userId, String courseId);

    void deleteAllByUserId(String userId);

    long countByUserId(String userId);
}
