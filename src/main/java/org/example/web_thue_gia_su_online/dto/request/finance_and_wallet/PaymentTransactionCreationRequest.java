package org.example.web_thue_gia_su_online.dto.request.finance_and_wallet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import org.example.web_thue_gia_su_online.enums.TransactionType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentTransactionCreationRequest {

    String sourceWalletId;
    String destinationWalletId;
    String bookingId;

    @NotNull
    TransactionType type;

    @NotNull
    @Positive
    Long amount;

    @NotNull
    Long balanceAfter;

    String note;
}
