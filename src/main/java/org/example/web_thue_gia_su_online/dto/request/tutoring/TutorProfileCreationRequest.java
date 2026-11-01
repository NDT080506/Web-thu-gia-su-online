package org.example.web_thue_gia_su_online.dto.request.tutoring;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorProfileCreationRequest {

    String bio;

    String subjectsCanTeach;
}
