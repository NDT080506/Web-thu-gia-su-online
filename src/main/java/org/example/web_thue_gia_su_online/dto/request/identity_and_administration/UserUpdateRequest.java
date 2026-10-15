package org.example.web_thue_gia_su_online.dto.request.identity_and_administration;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String email;
    String displayName;
    String avatarUrl;
    String phone;
    String zaloLink;
}
