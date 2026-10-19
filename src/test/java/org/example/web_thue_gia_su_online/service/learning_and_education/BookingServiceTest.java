package org.example.web_thue_gia_su_online.service.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BookingMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository repository;

    @Mock
    private BookingMapper mapper;

    @InjectMocks
    private BookingServiceImpl service;

    @Test
    void create_shouldReturnResponse() {
        BookingCreationRequest request = BookingCreationRequest.builder()
                .packageId("pkg-1")
                .studentId("student-1")
                .tutorId("tutor-1")
                .build();

        Booking booking = Booking.builder().id("1").build();
        BookingResponse response = BookingResponse.builder().id("1").build();
        when(mapper.toEntity(request)).thenReturn(booking);
        when(repository.save(any())).thenReturn(booking);
        when(mapper.toResponse(any())).thenReturn(response);

        BookingResponse result = service.create(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getById_shouldReturnResponse() {
        Booking booking = Booking.builder().id("1").build();
        BookingResponse response = BookingResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(booking));
        when(mapper.toResponse(booking)).thenReturn(response);

        BookingResponse result = service.getById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getById_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById("1"));
    }

    @Test
    void getByStudentId_shouldReturnList() {
        Booking booking = Booking.builder().id("1").build();
        BookingResponse response = BookingResponse.builder().id("1").build();
        when(repository.findByStudentId("student-1")).thenReturn(List.of(booking));
        when(mapper.toResponse(booking)).thenReturn(response);

        List<BookingResponse> result = service.getByStudentId("student-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getByTutorId_shouldReturnList() {
        Booking booking = Booking.builder().id("1").build();
        BookingResponse response = BookingResponse.builder().id("1").build();
        when(repository.findByTutorId("tutor-1")).thenReturn(List.of(booking));
        when(mapper.toResponse(booking)).thenReturn(response);

        List<BookingResponse> result = service.getByTutorId("tutor-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void update_shouldReturnResponse() {
        Booking booking = Booking.builder().id("1").build();
        BookingResponse response = BookingResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(booking));
        when(repository.save(any())).thenReturn(booking);
        when(mapper.toResponse(any())).thenReturn(response);

        BookingResponse result = service.update("1", any());

        assertNotNull(result);
        verify(repository).save(any());
    }
}
