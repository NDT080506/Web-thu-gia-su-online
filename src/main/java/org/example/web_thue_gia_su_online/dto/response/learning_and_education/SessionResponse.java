package org.example.web_thue_gia_su_online.dto.response.learning_and_education;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SessionResponse {
    String id;

    String order_id;

    Instant start_time;

    Instant end_time;

    String meet_url;

    boolean is_completed;

    Instant created_at;

    Instant updateAt;
}
