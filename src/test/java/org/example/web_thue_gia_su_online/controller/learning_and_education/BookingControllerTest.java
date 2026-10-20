package org.example.web_thue_gia_su_online.controller.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    void create_shouldReturnResponse() {
        BookingCreationRequest request = BookingCreationRequest.builder()
                .packageId("pkg-1")
                .studentId("student-1")
                .totalPrice(100000L)
                .build();
        BookingResponse response = BookingResponse.builder().id("1").build();
        when(bookingService.create(eq(request))).thenReturn(response);
        ApiResponse<BookingResponse> apiResponse = bookingController.create(request);
        assertNotNull(apiResponse);
        verify(bookingService).create(eq(request));
    }

    @Test
    void getById_shouldReturnResponse() {
        BookingResponse response = BookingResponse.builder().id("1").build();
        when(bookingService.getById("1")).thenReturn(response);
        ApiResponse<BookingResponse> apiResponse = bookingController.getById("1");
        assertNotNull(apiResponse);
        verify(bookingService).getById("1");
    }

    @Test
    void getByStudentId_shouldReturnList() {
        List<BookingResponse> list = List.of(BookingResponse.builder().id("1").build());
        when(bookingService.getByStudentId("student-1")).thenReturn(list);
        ApiResponse<List<BookingResponse>> apiResponse = bookingController.getByStudentId("student-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getByTutorId_shouldReturnList() {
        List<BookingResponse> list = List.of(BookingResponse.builder().id("1").build());
        when(bookingService.getByTutorId("tutor-1")).thenReturn(list);
        ApiResponse<List<BookingResponse>> apiResponse = bookingController.getByTutorId("tutor-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void update_shouldReturnResponse() {
        BookingUpdateRequest request =
                BookingUpdateRequest.builder().status("COMPLETED").build();
        BookingResponse response = BookingResponse.builder().id("1").build();
        when(bookingService.update(eq("1"), eq(request))).thenReturn(response);
        ApiResponse<BookingResponse> apiResponse = bookingController.update("1", request);
        assertNotNull(apiResponse);
        verify(bookingService).update(eq("1"), eq(request));
    }
}
