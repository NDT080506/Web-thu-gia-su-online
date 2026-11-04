package org.example.web_thue_gia_su_online.dto.response.identity_and_administration;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.ActionCode;

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

    String userId;

    String userDisplayName;

    ActionCode action;

    String targetType;

    String targetId;

    String ipAddress;

    String userAgent;

    Instant createdAt;
}
