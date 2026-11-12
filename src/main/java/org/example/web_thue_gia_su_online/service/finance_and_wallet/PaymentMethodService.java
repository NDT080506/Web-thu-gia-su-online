package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentMethodCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentMethodService {

    PaymentMethodResponse createPaymentMethod(PaymentMethodCreationRequest request);

    PaymentMethodResponse getPaymentMethod(String id);

    Page<PaymentMethodResponse> getPaymentMethodsByUserId(String userId, Pageable pageable);

    PaymentMethodResponse setDefaultPaymentMethod(String id);

    void deletePaymentMethod(String id);
}
