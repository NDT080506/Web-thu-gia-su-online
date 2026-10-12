package org.example.web_thue_gia_su_online.repository.finance_and_wallet;

import java.util.List;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, String> {
    List<PaymentTransaction> findByWalletId(String walletId);

    List<PaymentTransaction> findByOrderId(String orderId);
}