package org.example.web_thue_gia_su_online.entity.finance_and_wallet;

import java.time.Instant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.PaymentGatewayType;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.TransactionType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "payment_transactions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_wallet_id")
    Wallet sourceWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_wallet_id")
    Wallet destinationWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id")
    CourseEnrollment enrollment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdrawal_id", unique = true)
    private Withdrawal withdrawal;

    @Enumerated(EnumType.STRING)
    @NotNull
    TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    TransactionStatus status = TransactionStatus.PENDING;

    @Enumerated(EnumType.STRING)
    PaymentGatewayType provider;

    @Column(nullable = false, unique = true, length = 100)
    String referenceCode;

    @Column(length = 150)
    String providerReference;

    @NotNull
    Long amount;

    Long balanceAfter;

    @Column(columnDefinition = "TEXT")
    String note;

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
}
