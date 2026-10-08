package org.example.web_thue_gia_su_online.dto.request.learning_and_education;

import org.example.web_thue_gia_su_online.enums.OrderStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderUpdateRequest {

    OrderStatus status;
    long total_price;
    int sessions_remaining;
}
