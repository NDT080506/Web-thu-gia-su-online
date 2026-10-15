package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByStudentId(String studentId);

    List<Booking> findByTutorId(String tutorId);
}
