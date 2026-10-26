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
public class ReviewResponse {
    String id;
    String reviewerId;
    String tutorId;

    String bookingId;
    String enrollmentId;

    Integer rating;
    String comment;

    Instant createdAt;
    Instant updatedAt;
}
