package org.example.web_thue_gia_su_online.entity.finance_and_wallet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.WalletType;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "wallets", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "type"}))
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    @NotNull
    WalletType type;

    @Builder.Default
    long availableBalance = 0;

    @Builder.Default
    long holdingBalance = 0;

    @NotNull
    Instant createdAt;

    @NotNull
    Instant updatedAt;

    @OneToMany(mappedBy = "sourceWallet")
    @Builder.Default
    List<PaymentTransaction> outgoingTransactions = new ArrayList<>();

    @OneToMany(mappedBy = "destinationWallet")
    @Builder.Default
    List<PaymentTransaction> incomingTransactions = new ArrayList<>();
}
