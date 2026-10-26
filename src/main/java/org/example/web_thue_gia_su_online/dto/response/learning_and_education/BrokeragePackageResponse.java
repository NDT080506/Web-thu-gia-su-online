package org.example.web_thue_gia_su_online.dto.response.learning_and_education;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokeragePackageResponse {
    String id;
    String tutorId;
    String name;
    long price;
    int platformFeePercent;
    boolean active;
}
