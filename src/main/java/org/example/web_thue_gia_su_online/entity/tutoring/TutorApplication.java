package org.example.web_thue_gia_su_online.entity.tutoring;

import java.time.Instant;

import jakarta.persistence.*;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.RequestStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tutor_applications", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "status"}))
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

    @NotNull
    @Column(columnDefinition = "TEXT")
    String cvUrl;

    @Column(columnDefinition = "TEXT")
    String testSubmission;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @NotNull
    RequestStatus status = RequestStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    String adminNote;

    Instant createdAt;
}
