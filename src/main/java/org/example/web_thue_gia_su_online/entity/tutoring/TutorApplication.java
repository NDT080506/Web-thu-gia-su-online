package org.example.web_thue_gia_su_online.entity.tutoring;

import java.time.Instant;

import jakarta.persistence.*;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.ApplicationStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String cvUrl;

    String textSubmission;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    ApplicationStatus status = ApplicationStatus.PENDING;

    String adminNote;

    Instant createdAt;
}
