package org.example.web_thue_gia_su_online.repository.learning_and_education;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
    Page<Lesson> findByCourseId(String courseId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse(
			l.id,
			l.course.id,
			l.title,
			l.mediaUrl,
			l.meetingUrl,
			l.startsAt,
			l.createdAt
		)
		FROM Lesson l
		WHERE l.course.id = :courseId
		ORDER BY l.lessonOrder ASC
		""")
    Page<LessonResponse> findResponsesByCourseId(@Param("courseId") String courseId, Pageable pageable);
}
