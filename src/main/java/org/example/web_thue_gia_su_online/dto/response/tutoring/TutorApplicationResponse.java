package org.example.web_thue_gia_su_online.dto.response.tutoring;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.tutoring.ApplicationRequestStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorApplicationResponse {

    String id;
    String userId;
    String userName;
    String userEmail;
    String bio;
    String subjectsCanTeach;
    String cvUrl;
    String testSubmission;
    ApplicationRequestStatus status;
    String adminNote;
    Instant createdAt;
}
