package org.example.web_thue_gia_su_online.entity.finance_and_wallet;

import jakarta.persistence.*;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "payment_method", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "account_number"}))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(length = 100)
    String bankName;

    @Column(length = 50)
    String accountNumber;

    @Column(length = 150)
    String accountHolderName;

    @Builder.Default
    boolean isDefault = false;

    @NonNull
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
