package org.example.web_thue_gia_su_online.dto.request.learning_and_education;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseProgressUpdateRequest {

    @NotNull(message = "Tỷ lệ tiến độ không được để trống")
    @Min(value = 0, message = "Tiến độ phải từ 0% trở lên")
    @Max(value = 100, message = "Tiến độ không được vượt quá 100%")
    Integer progressPercent;
}
