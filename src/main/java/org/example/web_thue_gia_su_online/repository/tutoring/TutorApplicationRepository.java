package org.example.web_thue_gia_su_online.repository.tutoring;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorApplicationRepository extends JpaRepository<TutorApplication, String> {
    List<TutorApplication> findByUserId(String userId);

    Optional<TutorApplication> findByUserIdAndStatus(String userId, RequestStatus status);
}
