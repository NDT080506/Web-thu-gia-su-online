package org.example.web_thue_gia_su_online.dto.response.learning_and_education;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseResponse {
    String id;
    String tutorId;
    String title;
    long price;
    boolean isActive;
    Instant createdAt;
}
