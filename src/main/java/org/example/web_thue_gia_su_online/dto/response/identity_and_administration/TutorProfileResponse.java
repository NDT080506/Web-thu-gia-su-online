package org.example.web_thue_gia_su_online.dto.response.identity_and_administration;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorProfileResponse {

    String userId;
    String bio;
    String subjectsCanTeach;
    BigDecimal rating;
    Instant createdAt;
}
