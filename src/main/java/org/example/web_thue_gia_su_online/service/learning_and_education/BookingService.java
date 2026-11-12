package org.example.web_thue_gia_su_online.service.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    BookingResponse create(BookingCreationRequest request);

    BookingResponse getById(String id);

    Page<BookingResponse> getByMyId(Pageable pageable);

    //    Page<BookingResponse> getByTutorId(Pageable pageable);

    Page<BookingResponse> getByStudentId(String studentId, Pageable pageable);

    //    Page<BookingResponse> getByTutorId(String tutorId, Pageable pageable);

    BookingResponse cancelBooking(String bookingId);

    BookingResponse completeBooking(String bookingId);
}
