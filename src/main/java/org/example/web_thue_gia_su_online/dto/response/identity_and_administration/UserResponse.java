package org.example.web_thue_gia_su_online.dto.response.identity_and_administration;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.AccountStatus;
import org.example.web_thue_gia_su_online.enums.UserRole;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;

    String email;

    String displayName;

    UserRole role;

    AccountStatus status;

    String phone;
    String zaloLink;

    Instant createdAt;

    Instant updateAt;
}
