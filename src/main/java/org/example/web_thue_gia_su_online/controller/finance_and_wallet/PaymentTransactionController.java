package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentTransactionService;
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

    @PostMapping("/{walletId}")
    ApiResponse<PaymentTransactionResponse> createTransaction(
            @RequestBody @Valid PaymentTransactionCreationRequest request,
            @PathVariable String walletId,
            @RequestParam(required = false) String orderId) {
        return ApiResponse.<PaymentTransactionResponse>builder()
                .result(paymentTransactionService.createTransaction(request, walletId, orderId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<PaymentTransactionResponse> getTransaction(@PathVariable String id) {
        return ApiResponse.<PaymentTransactionResponse>builder()
                .result(paymentTransactionService.getTransaction(id))
                .build();
    }

    @GetMapping("/wallet/{walletId}")
    ApiResponse<List<PaymentTransactionResponse>> getTransactionsByWallet(@PathVariable String walletId) {
        return ApiResponse.<List<PaymentTransactionResponse>>builder()
                .result(paymentTransactionService.getTransactionsByWallet(walletId))
                .build();
    }

    @GetMapping("/order/{orderId}")
    ApiResponse<List<PaymentTransactionResponse>> getTransactionsByOrder(@PathVariable String orderId) {
        return ApiResponse.<List<PaymentTransactionResponse>>builder()
                .result(paymentTransactionService.getTransactionsByOrder(orderId))
                .build();
    }

    @GetMapping
    ApiResponse<List<PaymentTransactionResponse>> getTransactions() {
        return ApiResponse.<List<PaymentTransactionResponse>>builder()
                .result(paymentTransactionService.getTransactions())
                .build();
    }
}
