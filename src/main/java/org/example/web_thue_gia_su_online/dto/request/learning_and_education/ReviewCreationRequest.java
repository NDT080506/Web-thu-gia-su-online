package org.example.web_thue_gia_su_online.dto.request.learning_and_education;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewCreationRequest {

    String bookingId;

    String enrollmentId;

    @NonNull
    @Min(1)
    @Max(5)
    Integer rating;

    @NonNull
    @Size(max = 5000)
    String comment;
}
