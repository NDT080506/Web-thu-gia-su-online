package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.BookingService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;

    @PostMapping("/me")
    public ApiResponse<BookingResponse> create(@RequestBody @Valid BookingCreationRequest request) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.create(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<BookingResponse>> getByMyId() {
        return ApiResponse.<List<BookingResponse>>builder()
                .result(bookingService.getByMyId())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> getById(@PathVariable String id) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.getById(id))
                .build();
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<BookingResponse> cancelBooking(@PathVariable String id) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.update(id))
                .build();
    }

    @GetMapping("/tutor")
    public ApiResponse<List<BookingResponse>> getByTutorId() {
        return ApiResponse.<List<BookingResponse>>builder()
                .result(bookingService.getByTutorId())
                .build();
    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<List<BookingResponse>> getByStudentId(@PathVariable String studentId) {
        return ApiResponse.<List<BookingResponse>>builder()
                .result(bookingService.getByStudentId(studentId))
                .build();
    }

    @GetMapping("/tutor/{tutorId}")
    public ApiResponse<List<BookingResponse>> getByTutorId(@PathVariable String tutorId) {
        return ApiResponse.<List<BookingResponse>>builder()
                .result(bookingService.getByTutorId(tutorId))
                .build();
    }
}
