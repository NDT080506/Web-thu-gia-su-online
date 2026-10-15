package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, String> {
    List<CourseEnrollment> findByStudentId(String studentId);

    List<CourseEnrollment> findByCourseId(String courseId);

    Optional<CourseEnrollment> findByStudentIdAndCourseId(String studentId, String courseId);
}
