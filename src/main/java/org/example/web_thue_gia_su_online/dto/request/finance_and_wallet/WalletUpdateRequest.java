package org.example.web_thue_gia_su_online.dto.request.finance_and_wallet;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletUpdateRequest {

    Long availableBalance;
    Long holdingBalance;
}
