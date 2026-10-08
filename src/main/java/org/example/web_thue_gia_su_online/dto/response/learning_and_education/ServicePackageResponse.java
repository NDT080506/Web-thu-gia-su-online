package org.example.web_thue_gia_su_online.dto.response.learning_and_education;

import java.time.Instant;

import org.example.web_thue_gia_su_online.enums.PackageType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicePackageResponse {

    String id;

    String name;

    PackageType type;

    long price;

    int minSessions;

    int platformFeePercent;

    Instant createdAt;

    Instant updateAt;
}
