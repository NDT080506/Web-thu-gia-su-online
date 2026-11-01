package org.example.web_thue_gia_su_online.repository.tutoring;

import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfile, String> {
    boolean existsByUserId(String userId);

    Optional<TutorProfile> findByUserId(String userId);
}
