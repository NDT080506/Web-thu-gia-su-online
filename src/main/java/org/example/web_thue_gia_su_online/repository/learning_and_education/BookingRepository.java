package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    Optional<Booking> findByStudentId(String studentId);

    Optional<Booking> findByTutorId(String tutorId);

    List<Booking> findAllByStudentId(String studentId);

    List<Booking> findAllByTutorId(String tutorId);
}
