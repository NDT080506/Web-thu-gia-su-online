package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;

import jakarta.persistence.*;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

import lombok.*;

@Entity
@Table(
        name = "reviews",
        indexes = {
            @Index(name = "idx_reviews_tutor_created", columnList = "tutor_id, created_at"),
            @Index(name = "idx_reviews_reviewer_created", columnList = "reviewer_id, created_at")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reviewer_id", nullable = false)
    User reviewer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutor_id", nullable = false)
    User tutor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", unique = true)
    Booking booking;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", unique = true)
    CourseEnrollment enrollment;

    @Column(name = "rating", nullable = false)
    Integer rating;

    @Column(name = "comment", columnDefinition = "text")
    String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @Column(name = "updated_at")
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
