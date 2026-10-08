package org.example.web_thue_gia_su_online.repository.tutoring;

import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorApplicationRepository extends JpaRepository<TutorApplication, String> {}
