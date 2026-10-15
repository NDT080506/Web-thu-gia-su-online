package org.example.web_thue_gia_su_online.dto.request.learning_and_education;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingCreationRequest {
    @NotBlank
    String packageId;

    @NotBlank
    String studentId;

    String tutorId;

    String subject;

    @NotNull
    Long totalPrice;
}
