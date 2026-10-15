package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
    List<Lesson> findByCourseId(String courseId);
}
