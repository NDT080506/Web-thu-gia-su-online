package org.example.web_thue_gia_su_online.dto.request.finance_and_wallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentInitiationRequest {

    @NotBlank
    String transactionId;

    @NotBlank
    String referenceCode;

    @NotNull
    @Positive
    Long amount;

    @NotBlank
    String orderInfo;

    @NotBlank
    String returnUrl;
}
