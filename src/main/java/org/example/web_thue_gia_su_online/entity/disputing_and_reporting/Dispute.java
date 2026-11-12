package org.example.web_thue_gia_su_online.entity.disputing_and_reporting;

import java.time.Instant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.disputing_and_reporting.DisputeStatus;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    CourseEnrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    User createdBy;

    @NotNull
    @Column(columnDefinition = "TEXT")
    String reason;

    @NotNull
    @Column(columnDefinition = "TEXT")
    String evidenceUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    DisputeStatus status;

    @Builder.Default
    long refundAmount = 0;

    @Column(columnDefinition = "TEXT")
    String adminNote;

    @NotNull
    Instant createdAt;

    Instant resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
