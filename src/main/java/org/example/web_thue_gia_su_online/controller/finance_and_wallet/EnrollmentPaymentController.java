package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.EnrollmentPaymentResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.EnrollmentPaymentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/enrollment-payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentPaymentController {

    EnrollmentPaymentService enrollmentPaymentService;

    @PostMapping("/{enrollmentId}/payment")
    public ApiResponse<EnrollmentPaymentResponse> payForBooking(@PathVariable String enrollmentId) {
        return ApiResponse.<EnrollmentPaymentResponse>builder()
                .result(enrollmentPaymentService.payForEnrollment(enrollmentId))
                .build();
    }
}
