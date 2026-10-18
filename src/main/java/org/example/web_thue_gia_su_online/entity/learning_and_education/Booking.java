package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    BrokeragePackage brokeragePackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id")
    User tutor;

    @Column(length = 100)
    String subject;

    @Column(length = 50, nullable = false)
    @Builder.Default
    String status = "pending_payment";

    @NotNull
    long totalPrice;

    @NotNull
    long platformFee;

    @NotNull
    long tutorIncome;

    Instant contactUnlockedAt;

    Instant studentConfirmedAt;

    Integer studentRating;

    @Column(columnDefinition = "TEXT")
    String studentReview;

    @NotNull
    Instant createdAt;

    @NotNull
    Instant updatedAt;

    // ─── One-to-Many ──────────────────────────────────────────
    @OneToMany(mappedBy = "booking")
    @Builder.Default
    List<Message> messages = new ArrayList<>();

    @OneToOne(mappedBy = "booking")
    Dispute dispute;

    @OneToMany(mappedBy = "booking")
    @Builder.Default
    List<PaymentTransaction> paymentTransactions = new ArrayList<>();
}
