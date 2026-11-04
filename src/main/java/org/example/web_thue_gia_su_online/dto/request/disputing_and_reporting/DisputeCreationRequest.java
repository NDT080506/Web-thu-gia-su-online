package org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DisputeCreationRequest {

    String bookingId;

    String enrollmentId;

    @NotBlank
    String reason;

    @NotBlank
    String evidenceUrl;
}
