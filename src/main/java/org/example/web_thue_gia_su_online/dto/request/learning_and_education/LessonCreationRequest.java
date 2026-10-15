package org.example.web_thue_gia_su_online.dto.request.learning_and_education;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LessonCreationRequest {
    @NotBlank
    String courseId;

    @NotBlank
    String title;

    String mediaUrl;
    String meetingUrl;
    Instant startsAt;
}
