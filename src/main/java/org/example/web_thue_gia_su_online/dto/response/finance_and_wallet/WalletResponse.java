package org.example.web_thue_gia_su_online.dto.response.finance_and_wallet;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletResponse {

    String id;
    String userId;
    long balance;
    String bankInfo;
    Instant createdAt;
    Instant updateAt;
}
