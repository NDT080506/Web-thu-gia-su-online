package org.example.web_thue_gia_su_online.controller.learning_and_education;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping
    public ApiResponse<BookingResponse> createRoot(@RequestBody @Valid BookingCreationRequest request) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.create(request))
                .build();
    }

    @PostMapping("/me")
    public ApiResponse<BookingResponse> create(@RequestBody @Valid BookingCreationRequest request) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.create(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<Page<BookingResponse>> getByMyId(Pageable pageable) {
        return ApiResponse.<Page<BookingResponse>>builder()
                .result(bookingService.getByMyId(pageable))
                .build();
    }

    @GetMapping("/student")
    public ApiResponse<Page<BookingResponse>> getByStudent(Pageable pageable) {
        return ApiResponse.<Page<BookingResponse>>builder()
                .result(bookingService.getByMyId(pageable))
                .build();
    }

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponse> getById(@PathVariable String bookingId) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.getById(bookingId))
                .build();
    }

    @PatchMapping("/{bookingId}/cancel")
    public ApiResponse<BookingResponse> cancelBooking(@PathVariable String bookingId) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.cancelBooking(bookingId))
                .build();
    }

    @PatchMapping("/{bookingId}/complete")
    public ApiResponse<BookingResponse> completeBooking(@PathVariable String bookingId) {
        return ApiResponse.<BookingResponse>builder()
                .result(bookingService.completeBooking(bookingId))
                .build();
    }

    //    @GetMapping("/tutor")
    //    public ApiResponse<List<BookingResponse>> getByTutorId() {
    //        return ApiResponse.<List<BookingResponse>>builder()
    //                .result(bookingService.getByTutorId())
    //                .build();
    //    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<Page<BookingResponse>> getByStudentId(@PathVariable String studentId, Pageable pageable) {
        return ApiResponse.<Page<BookingResponse>>builder()
                .result(bookingService.getByStudentId(studentId, pageable))
                .build();
    }

    //    @GetMapping("/tutor/{tutorId}")
    //    public ApiResponse<Page<BookingResponse>> getByTutorId(@PathVariable String tutorId, Pageable pageable) {
    //        return ApiResponse.<Page<BookingResponse>>builder()
    //                .result(bookingService.getByTutorId(tutorId, pageable))
    //                .build();
    //    }
}
