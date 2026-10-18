package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "brokerage_packages")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokeragePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NotNull
    @Column(nullable = false, length = 255)
    String name;

    @NotNull
    long price;

    @NotNull
    int platformFeePercent;

    @NotNull
    boolean isActive;

    @NotNull
    Instant createdAt;

    @NotNull
    Instant updatedAt;

    @OneToMany(mappedBy = "brokeragePackage")
    @Builder.Default
    List<Booking> bookings = new ArrayList<>();
}
