package org.example.web_thue_gia_su_online.dto.response.learning_and_education;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.OrderStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

    String id;
    String userId;
    String tutorId;
    String packageId;

    OrderStatus status;

    long total_price;
    int sessions_remaining;

    Instant created_at;

    Instant updateAt;
}
