package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentInitiationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentInitiationResult;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentVerificationResult;
import org.example.web_thue_gia_su_online.enums.PaymentGatewayType;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentGateway;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MockPaymentGateway implements PaymentGateway {

    ConcurrentMap<String, MockPaymentState> payments = new ConcurrentHashMap<>();

    @Override
    public PaymentGatewayType getType() {
        return PaymentGatewayType.MOCK;
    }

    @Override
    public PaymentInitiationResult initiatePayment(PaymentInitiationRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        String providerReference = "MOCK-" + UUID.randomUUID();

        MockPaymentState state =
                new MockPaymentState(request.getReferenceCode(), providerReference, request.getAmount(), null);

        MockPaymentState existing = payments.putIfAbsent(request.getReferenceCode(), state);

        if (existing != null) {
            throw new IllegalArgumentException("Payment reference already exists");
        }

        String paymentUrl = "http://localhost:8080/giasu/mock-payment/" + request.getReferenceCode();

        return new PaymentInitiationResult(
                request.getTransactionId(), request.getReferenceCode(), providerReference, paymentUrl);
    }

    @Override
    public PaymentVerificationResult verifyPayment(String referenceCode) {
        MockPaymentState state = payments.get(referenceCode);

        if (state == null || state.result() == null) {
            return new PaymentVerificationResult(
                    referenceCode,
                    state == null ? null : state.providerReference(),
                    state == null ? null : state.amount(),
                    false,
                    false);
        }

        return new PaymentVerificationResult(
                state.referenceCode(), state.providerReference(), state.amount(), true, state.result());
    }

    public void simulateResult(String referenceCode, boolean successful) {
        payments.compute(referenceCode, (key, current) -> {
            if (current == null) {
                throw new IllegalArgumentException("Payment reference not found");
            }

            if (current.result() != null) {
                throw new IllegalStateException("Payment result has already been set");
            }

            return new MockPaymentState(
                    current.referenceCode(), current.providerReference(), current.amount(), successful);
        });
    }

    private record MockPaymentState(String referenceCode, String providerReference, Long amount, Boolean result) {}
}
