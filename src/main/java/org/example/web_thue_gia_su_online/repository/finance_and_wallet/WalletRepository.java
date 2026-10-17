package org.example.web_thue_gia_su_online.repository.finance_and_wallet;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUserId(String userId);

    Optional<Wallet> findByUserIdAndType(String userId, WalletType type);

    Boolean existsByUserIdAndType(String userId, WalletType type);

    List<Wallet> findAllByUserId(String userId);

    Optional<Wallet> findByType(WalletType type);
}
