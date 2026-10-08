package org.example.web_thue_gia_su_online.dto.request.identity_and_administration;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorProfileUpdateRequest {

    String bio;

    String subjectsCanTeach;
}
