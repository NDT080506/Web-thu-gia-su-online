package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.learning_and_education.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, String> {

    List<WishlistItem> findAllByUserIdOrderByCreatedAtDesc(String userId);

    Optional<WishlistItem> findByUserIdAndCourseId(String userId, String courseId);

    boolean existsByUserIdAndCourseId(String userId, String courseId);

    void deleteByUserIdAndCourseId(String userId, String courseId);

    long countByUserId(String userId);
}
