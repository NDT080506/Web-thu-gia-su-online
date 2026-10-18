package org.example.web_thue_gia_su_online.dto.request.tutoring;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorApplicationCreationRequest {

    @NotBlank
    String bio;

    @NotBlank
    String subjectsCanTeach;

    @NotBlank
    String cvUrl;

    @NotBlank
    String testSubmission;
}
