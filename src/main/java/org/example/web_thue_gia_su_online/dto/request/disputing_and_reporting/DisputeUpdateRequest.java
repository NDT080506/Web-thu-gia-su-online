package org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting;

import org.example.web_thue_gia_su_online.enums.DisputeStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DisputeUpdateRequest {

    DisputeStatus status;
    String resolutionNote;
}
