package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.TransactionType;

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
    String walletId;
    String orderId;
    TransactionType type;
    long amount;
    String bankRefCode;
    Instant createdAt;
    Instant updateAt;
}
