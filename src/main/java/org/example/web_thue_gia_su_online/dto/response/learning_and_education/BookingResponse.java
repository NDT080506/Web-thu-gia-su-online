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
public class BookingResponse {
    String id;
    String packageId;
    String studentId;
    String tutorId;
    String subject;
    String status;
    long totalPrice;
    long platformFee;
    long tutorIncome;
    Instant contactUnlockedAt;
    Instant studentConfirmedAt;
    Integer studentRating;
    String studentReview;
    Instant createdAt;
    Instant updatedAt;
}
