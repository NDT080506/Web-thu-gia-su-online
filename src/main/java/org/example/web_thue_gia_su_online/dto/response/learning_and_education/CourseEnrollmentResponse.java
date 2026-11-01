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
public class CourseEnrollmentResponse {
    String id;
    String studentId;
    String courseId;
    long pricePaid;
    String status;
    int progressPercent;
    Instant completedAt;
    Instant createdAt;
}
