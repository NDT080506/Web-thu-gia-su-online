package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentPaymentResponse {
    String transactionId;
    String enrollmentId;
    String referenceCode;
    long amount;
    long availableBalance;
    long holdingBalance;
    TransactionType type;
    TransactionStatus status;
    Instant createdAt;
}
