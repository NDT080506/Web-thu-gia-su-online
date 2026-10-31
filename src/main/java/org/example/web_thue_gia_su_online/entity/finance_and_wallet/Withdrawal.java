package org.example.web_thue_gia_su_online.entity.finance_and_wallet;

import java.time.Instant;

import jakarta.persistence.*;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.WithdrawalStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "withdrawal")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountHolder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithdrawalStatus status;

    private String providerReference;

    private String failureReason;

    private Instant createdAt;

    private Instant processedAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();

        if (status == null) {
            status = WithdrawalStatus.PENDING;
        }
    }
}
