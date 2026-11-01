package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import org.example.web_thue_gia_su_online.enums.PaymentGatewayType;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepositResponse {

    String transactionId;
    String referenceCode;
    Long amount;
    TransactionStatus status;
    PaymentGatewayType provider;
    String paymentUrl;
}
