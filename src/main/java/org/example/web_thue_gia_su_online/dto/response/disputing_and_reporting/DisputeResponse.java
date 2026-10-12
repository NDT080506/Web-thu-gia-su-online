package org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.DisputeStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DisputeResponse {

    String id;
    String orderId;
    String sessionId;
    String raisedById;
    String reasonType;
    String description;
    DisputeStatus status;
    String adminId;
    String resolutionNote;
    Instant createdAt;
    Instant updateAt;
}