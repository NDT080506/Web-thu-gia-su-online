package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

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

    String name;

    PackageType type;

    long price;

    @Builder.Default
    int min_sessions = 1;

    @Builder.Default
    int platform_fee_percent = 15;

    Instant createdAt;

    Instant updateAt;

    @OneToMany(mappedBy = "servicePackage", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Order> servicePackages = new ArrayList<>();
}
