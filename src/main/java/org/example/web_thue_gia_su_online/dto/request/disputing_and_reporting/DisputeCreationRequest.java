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

    @NotBlank
    String bookingId;

    @NotBlank
    String studentId;

    @NotBlank
    String reason;
}
