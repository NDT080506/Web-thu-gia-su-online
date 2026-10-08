package org.example.web_thue_gia_su_online.repository.identity_and_administration;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.TutorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfile, String> {
    boolean existById(String userId);
}
