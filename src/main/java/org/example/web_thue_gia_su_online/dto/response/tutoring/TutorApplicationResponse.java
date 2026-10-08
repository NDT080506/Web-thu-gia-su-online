package org.example.web_thue_gia_su_online.dto.response.tutoring;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.ApplicationStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TutorApplicationResponse {

    String id;

    String userId;

    String cvUrl;

    String testSubmission;

    ApplicationStatus status;

    String adminNote;

    Instant createdAt;

    Instant updateAt;
}
