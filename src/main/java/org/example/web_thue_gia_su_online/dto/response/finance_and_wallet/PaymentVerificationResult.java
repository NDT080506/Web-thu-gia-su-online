package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationResult {
    String referenceCode;
    String providerReference;
    Long amount;
    boolean verified;
    boolean successful;
}
