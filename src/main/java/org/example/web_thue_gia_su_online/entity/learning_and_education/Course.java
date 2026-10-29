package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "courses")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    User tutor;

    @NotNull
    @Column(columnDefinition = "TEXT")
    String title;

    @NotNull
    long price;

    @NotNull
    int platformFeePercent;

    @NotNull
    @Column(name = "is_active")
    boolean active;

    @NotNull
    Instant createdAt;

    Instant updatedAt;

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    @Builder.Default
    List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    @Builder.Default
    List<CourseEnrollment> enrollments = new ArrayList<>();

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
