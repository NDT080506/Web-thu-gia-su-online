package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentMethodCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse;

public interface PaymentMethodService {

    PaymentMethodResponse createPaymentMethod(PaymentMethodCreationRequest request);

    PaymentMethodResponse getPaymentMethod(String id);

    List<PaymentMethodResponse> getPaymentMethodsByUserId(String userId);

    PaymentMethodResponse setDefaultPaymentMethod(String id);

    void deletePaymentMethod(String id);
}
