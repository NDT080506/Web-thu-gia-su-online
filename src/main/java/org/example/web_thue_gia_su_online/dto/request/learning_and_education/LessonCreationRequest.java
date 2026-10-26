package org.example.web_thue_gia_su_online.dto.request.learning_and_education;

import java.time.Instant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    String title;

    String mediaUrl;
    String meetingUrl;
    Instant startsAt;

    @NotNull
    @Min(1)
    private Integer lessonOrder;
}
