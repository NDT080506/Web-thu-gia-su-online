package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.learning_and_education.EnrollmentStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "course_enrollments", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @NotNull
    long totalPrice;

    Long pricePaid;

    @Builder.Default
    int progressPercent = 0;

    @NotNull
    long platformFee;

    @NotNull
    long tutorIncome;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    EnrollmentStatus status = EnrollmentStatus.PENDING;

    Instant completedAt;

    @NotNull
    Instant createdAt;

    Instant updatedAt;

    // ─── One-to-One/Many ──────────────────────────────────────────
    @OneToOne(mappedBy = "enrollment")
    private Review review;

    @OneToMany(mappedBy = "enrollment", orphanRemoval = true)
    List<Dispute> disputes;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
