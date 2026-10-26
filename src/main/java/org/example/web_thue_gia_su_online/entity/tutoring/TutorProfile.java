package org.example.web_thue_gia_su_online.entity.tutoring;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tutor_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorProfile {

    @Id
    String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    User user;

    @Column(columnDefinition = "TEXT")
    String bio;

    @Column(columnDefinition = "TEXT")
    String subjectsCanTeach;

    @Builder.Default
    @Column(precision = 3, scale = 2)
    BigDecimal rating = new BigDecimal("5.00");

    @NotNull
    Instant createdAt;

    Instant updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
