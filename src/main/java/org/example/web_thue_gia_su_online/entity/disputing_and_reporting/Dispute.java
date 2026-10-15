package org.example.web_thue_gia_su_online.entity.disputing_and_reporting;

import java.time.Instant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "disputes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Dispute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    User student;

    @NotNull
    @Column(columnDefinition = "TEXT")
    String reason;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(length = 50, nullable = false)
    DisputeStatus status = DisputeStatus.PENDING;

    @Builder.Default
    long refundAmount = 0;

    @Column(columnDefinition = "TEXT")
    String adminNote;

    @NotNull
    Instant createdAt;

    Instant resolvedAt;
}
