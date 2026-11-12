package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentTransactionResponse {

    String id;
    String sourceWalletId;
    String destinationWalletId;
    String bookingId;
    TransactionType type;
    Long amount;
    Long balanceAfter;
    String note;
    Instant createdAt;
}
