package org.example.web_thue_gia_su_online.repository.tutoring;

import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfile, String> {}
