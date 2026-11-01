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
    String bookingId;
    String studentId;
    String reason;
    DisputeStatus status;
    long refundAmount;
    String adminNote;
    Instant createdAt;
    Instant resolvedAt;
}
