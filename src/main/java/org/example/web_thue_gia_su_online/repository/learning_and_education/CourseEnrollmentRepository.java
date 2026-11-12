package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, String> {
    Page<CourseEnrollment> findByStudentId(String studentId, Pageable pageable);

    Page<CourseEnrollment> findByCourseId(String courseId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
		SELECT c
		FROM CourseEnrollment c
		WHERE c.student.id = :studentId
		AND c.course.id = :courseId
		""")
    Optional<CourseEnrollment> findByStudentIdAndCourseId(
            @Param("studentId") String studentId, @Param("courseId") String courseId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
		SELECT c
		FROM CourseEnrollment c
		WHERE c.id = :enrollmentId
		""")
    Optional<CourseEnrollment> findByIdForUpdate(@Param("enrollmentId") String enrollmentId);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse(
			ce.id,
			ce.student.id,
			ce.course.id,
			ce.pricePaid,
			ce.status,
			ce.progressPercent,
			ce.completedAt,
			ce.createdAt
		)
		FROM CourseEnrollment ce
		WHERE ce.id = :id
		""")
    Optional<CourseEnrollmentResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse(
			ce.id,
			ce.student.id,
			ce.course.id,
			ce.pricePaid,
			ce.status,
			ce.progressPercent,
			ce.completedAt,
			ce.createdAt
		)
		FROM CourseEnrollment ce
		WHERE ce.student.id = :studentId
		""")
    Page<CourseEnrollmentResponse> findResponsesByStudentId(@Param("studentId") String studentId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse(
			ce.id,
			ce.student.id,
			ce.course.id,
			ce.pricePaid,
			ce.status,
			ce.progressPercent,
			ce.completedAt,
			ce.createdAt
		)
		FROM CourseEnrollment ce
		WHERE ce.course.id = :courseId
		""")
    Page<CourseEnrollmentResponse> findResponsesByCourseId(@Param("courseId") String courseId, Pageable pageable);
}
