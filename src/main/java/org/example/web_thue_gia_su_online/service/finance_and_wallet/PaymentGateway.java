package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentInitiationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentInitiationResult;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentVerificationResult;
import org.example.web_thue_gia_su_online.enums.PaymentGatewayType;

public interface PaymentGateway {
    PaymentGatewayType getType();

    PaymentInitiationResult initiatePayment(PaymentInitiationRequest request);

    PaymentVerificationResult verifyPayment(String referenceCode);
}
