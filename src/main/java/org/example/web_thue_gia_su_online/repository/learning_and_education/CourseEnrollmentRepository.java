package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, String> {
    List<CourseEnrollment> findByStudentId(String studentId);

    List<CourseEnrollment> findByCourseId(String courseId);

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
}
