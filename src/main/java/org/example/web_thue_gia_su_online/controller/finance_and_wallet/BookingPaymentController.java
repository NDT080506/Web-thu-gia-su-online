package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.BookingPaymentResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.BookingPaymentService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/booking-payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingPaymentController {

    BookingPaymentService bookingPaymentService;

    @PostMapping("/{bookingId}/payment")
    public ApiResponse<BookingPaymentResponse> payForBooking(@PathVariable String bookingId) {
        return ApiResponse.<BookingPaymentResponse>builder()
                .result(bookingPaymentService.payForBooking(bookingId))
                .build();
    }
}
