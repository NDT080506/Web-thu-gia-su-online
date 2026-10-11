package org.example.web_thue_gia_su_online.repository.finance_and_wallet;

import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUserId(String userId);
}
