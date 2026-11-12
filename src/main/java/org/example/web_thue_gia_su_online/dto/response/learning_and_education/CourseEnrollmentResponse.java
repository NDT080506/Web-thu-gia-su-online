package org.example.web_thue_gia_su_online.dto.response.learning_and_education;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.learning_and_education.EnrollmentStatus;

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
    Long pricePaid;
    EnrollmentStatus status;
    Integer progressPercent;
    Instant completedAt;
    Instant createdAt;
}
