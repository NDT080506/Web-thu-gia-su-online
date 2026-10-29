package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.BookingStatus;

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

    @Enumerated(EnumType.STRING)
    @NotNull
    @Builder.Default
    BookingStatus status = BookingStatus.PENDING;

    @NotNull
    long totalPrice;

    @NotNull
    long platformFee;

    @NotNull
    long tutorIncome;

    @NotNull
    Instant createdAt;

    Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    // ─── One-to-Many ──────────────────────────────────────────
    @OneToOne(mappedBy = "booking")
    Review review;

    @OneToOne(mappedBy = "booking")
    Dispute dispute;

    @OneToMany(mappedBy = "booking")
    @Builder.Default
    List<PaymentTransaction> paymentTransactions = new ArrayList<>();

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }
}
