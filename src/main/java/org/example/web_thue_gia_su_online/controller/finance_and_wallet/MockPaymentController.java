package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentInitiationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentInitiationResult;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentVerificationResult;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.impl.MockPaymentGateway;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mock-payments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Profile("dev")
public class MockPaymentController {

    MockPaymentGateway mockPaymentGateway;

    @PostMapping("/initiate")
    public PaymentInitiationResult initiatePayment(@Valid @RequestBody PaymentInitiationRequest request) {
        return mockPaymentGateway.initiatePayment(request);
    }

    @GetMapping("/{referenceCode}")
    public PaymentVerificationResult getPayment(@PathVariable String referenceCode) {
        return mockPaymentGateway.verifyPayment(referenceCode);
    }

    @PostMapping("/{referenceCode}/success")
    public PaymentVerificationResult simulateSuccess(@PathVariable String referenceCode) {
        mockPaymentGateway.simulateResult(referenceCode, true);
        return mockPaymentGateway.verifyPayment(referenceCode);
    }

    @PostMapping("/{referenceCode}/failure")
    public PaymentVerificationResult simulateFailure(@PathVariable String referenceCode) {
        mockPaymentGateway.simulateResult(referenceCode, false);
        return mockPaymentGateway.verifyPayment(referenceCode);
    }
}
