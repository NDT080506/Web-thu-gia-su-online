package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;

public interface BookingService {

    BookingResponse create(BookingCreationRequest request);

    BookingResponse getById(String id);

    List<BookingResponse> getByMyId();

    List<BookingResponse> getByTutorId();

    List<BookingResponse> getByStudentId(String studentId);

    List<BookingResponse> getByTutorId(String tutorId);

    BookingResponse update(String id);
}
