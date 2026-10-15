package org.example.web_thue_gia_su_online.dto.response.identity_and_administration;

import java.time.Instant;
import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActionLogResponse {

    String id;
    String adminId;
    String targetUserId;
    String targetType;
    UUID targetId;
    String actionCode;
    String oldValue;
    String newValue;
    String reason;
    Instant createdAt;
}
