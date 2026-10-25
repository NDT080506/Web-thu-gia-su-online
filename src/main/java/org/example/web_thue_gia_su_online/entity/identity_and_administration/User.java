package org.example.web_thue_gia_su_online.entity.identity_and_administration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.example.web_thue_gia_su_online.entity.authentication.InvalidatedToken;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentMethod;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.*;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.example.web_thue_gia_su_online.enums.AccountStatus;
import org.example.web_thue_gia_su_online.enums.UserRole;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false, length = 255)
    String email;

    @Column(name = "email_verified_at")
    Instant emailVerifiedAt;

    @Column(nullable = false, length = 255)
    String passwordHash;

    @Column(nullable = false, length = 150)
    String displayName;

    @Column(columnDefinition = "TEXT")
    String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @NotNull
    UserRole role = UserRole.GUEST;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    AccountStatus status = AccountStatus.ACTIVE;

    @Column(length = 20)
    String phone;

    @Column(columnDefinition = "TEXT")
    String zaloLink;

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

    // ─── One-to-One ───────────────────────────────────────────
    @OneToOne(mappedBy = "user")
    TutorProfile tutorProfile;

    // ─── One-to-Many ──────────────────────────────────────────
    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<InvalidatedToken> invalidatedTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<PaymentMethod> paymentMethods = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<TutorApplication> tutorApplications = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    @Builder.Default
    List<Booking> studentBookings = new ArrayList<>();

    @OneToMany(mappedBy = "tutor")
    @Builder.Default
    List<Booking> tutorBookings = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    @Builder.Default
    List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "tutor")
    @Builder.Default
    List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    @Builder.Default
    List<CourseEnrollment> courseEnrollments = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    @Builder.Default
    List<Dispute> disputes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<Wallet> wallets = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    @Builder.Default
    List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    List<ArticleComment> articleComments = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer")
    List<Review> writtenReviews = new ArrayList<>();

    @OneToMany(mappedBy = "tutor")
    List<Review> receivedReviews = new ArrayList<>();


}
