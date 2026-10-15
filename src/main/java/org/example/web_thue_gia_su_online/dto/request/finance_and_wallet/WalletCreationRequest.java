package org.example.web_thue_gia_su_online.dto.request.finance_and_wallet;

import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.enums.WalletType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletCreationRequest {

    String userId;

    @NotNull
    WalletType type;
}
