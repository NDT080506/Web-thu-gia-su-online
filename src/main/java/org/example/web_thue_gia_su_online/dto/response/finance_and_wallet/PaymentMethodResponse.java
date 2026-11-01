package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethodResponse {

    String id;
    String userId;
    String bankName;
    String accountNumber;
    String accountHolderName;
    boolean isDefault;
}
