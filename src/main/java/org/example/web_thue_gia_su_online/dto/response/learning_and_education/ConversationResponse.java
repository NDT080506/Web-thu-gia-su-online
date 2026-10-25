package org.example.web_thue_gia_su_online.dto.response.learning_and_education;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.web_thue_gia_su_online.enums.ConversationStatus;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;

    String studentId;
    String studentName;

    String tutorId;
    String tutorName;

    ConversationStatus status;

    Instant tutorAgreedAt;
    Instant studentAgreedAt;

    Instant createdAt;
    Instant updatedAt;
}
