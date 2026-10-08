package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.enums.PackageType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NotNull
    String name;

    @NotNull
    PackageType type;

    @NotNull
    long price;

    @NotNull
    @Builder.Default
    int min_sessions = 1;

    @NotNull
    @Builder.Default
    int platform_fee_percent = 15;

    @NotNull
    Instant createdAt;

    @NonNull
    Instant updateAt;

    @OneToMany(mappedBy = "servicePackage", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Order> servicePackages = new ArrayList<>();
}
