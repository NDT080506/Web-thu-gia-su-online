package org.example.web_thue_gia_su_online.dto.request.learning_and_education;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseUpdateRequest {
    String title;
    long price;
    int platformFeePercent;
    boolean active;
}
