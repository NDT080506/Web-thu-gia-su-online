package org.example.web_thue_gia_su_online.dto.response.learning_and_education;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.learning_and_education.BookingStatus;

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
    String packageName;
    String studentId;
    String studentName;
    String tutorId;
    BookingStatus status;
    Long totalPrice;
    Long platformFee;
    Long tutorIncome;
    Instant createdAt;
    Instant updatedAt;
}
