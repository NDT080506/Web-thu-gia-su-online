package org.example.web_thue_gia_su_online.dto.request.finance_and_wallet;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethodCreationRequest {

    @NotBlank
    String userId;

    @NotBlank
    String bankName;

    @NotBlank
    String accountNumber;

    @NotBlank
    String accountHolderName;

    @Builder.Default
    Boolean isDefault = false;
}
