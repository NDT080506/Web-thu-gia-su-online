package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentMethodCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentMethodService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    PaymentMethodService paymentMethodService;

    @PostMapping
    ApiResponse<PaymentMethodResponse> createPaymentMethod(@RequestBody @Valid PaymentMethodCreationRequest request) {
        return ApiResponse.<PaymentMethodResponse>builder()
                .result(paymentMethodService.createPaymentMethod(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<PaymentMethodResponse> getPaymentMethod(@PathVariable String id) {
        return ApiResponse.<PaymentMethodResponse>builder()
                .result(paymentMethodService.getPaymentMethod(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<PaymentMethodResponse>> getPaymentMethodsByUserId(@PathVariable String userId) {
        return ApiResponse.<List<PaymentMethodResponse>>builder()
                .result(paymentMethodService.getPaymentMethodsByUserId(userId))
                .build();
    }

    @PutMapping("/{id}/default")
    ApiResponse<PaymentMethodResponse> setDefaultPaymentMethod(@PathVariable String id) {
        return ApiResponse.<PaymentMethodResponse>builder()
                .result(paymentMethodService.setDefaultPaymentMethod(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deletePaymentMethod(@PathVariable String id) {
        paymentMethodService.deletePaymentMethod(id);
        return ApiResponse.<Void>builder()
                .message("Payment method deleted successfully")
                .build();
    }
}
