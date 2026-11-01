package org.example.web_thue_gia_su_online.repository.tutoring;

import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorApplicationRepository extends JpaRepository<TutorApplication, String> {
    Optional<TutorApplication> findByUserId(String userId);

    boolean existsByUserIdAndStatus(String userId, RequestStatus status);
}
