package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Page<Course> findAllByTutorId(String tutorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse(
			c.id,
			c.tutor.id,
			c.tutor.displayName,
			c.title,
			c.price,
			c.active,
			c.createdAt
		)
		FROM Course c
		WHERE c.id = :id
		""")
    Optional<CourseResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse(
			c.id,
			c.tutor.id,
			c.tutor.displayName,
			c.title,
			c.price,
			c.active,
			c.createdAt
		)
		FROM Course c
		WHERE c.tutor.id = :tutorId
		""")
    Page<CourseResponse> findResponsesByTutorId(@Param("tutorId") String tutorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse(
			c.id,
			c.tutor.id,
			c.tutor.displayName,
			c.title,
			c.price,
			c.active,
			c.createdAt
		)
		FROM Course c
		""")
    Page<CourseResponse> findResponsesAll(Pageable pageable);
}
