package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/transactions")
public class PaymentTransactionController {

    PaymentTransactionService paymentTransactionService;

    @PostMapping
    ApiResponse<PaymentTransactionResponse> createTransaction(
            @RequestBody @Valid PaymentTransactionCreationRequest request) {
        return ApiResponse.<PaymentTransactionResponse>builder()
                .result(paymentTransactionService.createTransaction(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<PaymentTransactionResponse> getTransaction(@PathVariable String id) {
        return ApiResponse.<PaymentTransactionResponse>builder()
                .result(paymentTransactionService.getTransaction(id))
                .build();
    }

    @GetMapping("/wallet/{walletId}")
    ApiResponse<Page<PaymentTransactionResponse>> getTransactionsByWallet(
            @PathVariable String walletId, Pageable pageable) {
        return ApiResponse.<Page<PaymentTransactionResponse>>builder()
                .result(paymentTransactionService.getTransactionsByWallet(walletId, pageable))
                .build();
    }

    @GetMapping("/booking/{bookingId}")
    ApiResponse<Page<PaymentTransactionResponse>> getTransactionsByBooking(
            @PathVariable String bookingId, Pageable pageable) {
        return ApiResponse.<Page<PaymentTransactionResponse>>builder()
                .result(paymentTransactionService.getTransactionsByBooking(bookingId, pageable))
                .build();
    }

    @GetMapping
    ApiResponse<Page<PaymentTransactionResponse>> getTransactions(Pageable pageable) {
        return ApiResponse.<Page<PaymentTransactionResponse>>builder()
                .result(paymentTransactionService.getTransactions(pageable))
                .build();
    }
}
