package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.WithdrawalStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WithdrawalResponse {

    String id;

    Long amount;

    String bankName;

    String accountNumber;

    String accountHolder;

    WithdrawalStatus status;

    String providerReference;

    String failureReason;

    Instant createdAt;

    Instant processedAt;
}
