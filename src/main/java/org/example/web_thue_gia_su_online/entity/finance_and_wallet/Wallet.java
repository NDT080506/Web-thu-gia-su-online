package org.example.web_thue_gia_su_online.entity.finance_and_wallet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @NotNull
    @Builder.Default
    long balance = 0;

    @Column(columnDefinition = "jsonb")
    String bankInfo;

    @NotNull
    Instant createdAt;

    @NonNull
    Instant updateAt;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<PaymentTransaction> paymentTransactions = new ArrayList<>();
}
