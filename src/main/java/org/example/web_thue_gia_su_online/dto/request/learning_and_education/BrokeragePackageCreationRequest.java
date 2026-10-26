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
public class BrokeragePackageCreationRequest {
    @NotBlank
    String name;

    @NotNull
    long price;

    @NotNull
    int platformFeePercent;
}
