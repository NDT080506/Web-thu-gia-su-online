package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationResult {
    String transactionId;
    String referenceCode;
    String providerReference;
    String paymentUrl;
}
