package org.example.web_thue_gia_su_online.repository.finance_and_wallet;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUserId(String userId);

    Optional<Wallet> findByUser_IdAndType(String userId, WalletType type);

    Boolean existsByUser_IdAndType(String userId, WalletType type);

    Optional<Wallet> findFirstByType(WalletType type);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :walletId")
    Optional<Wallet> findByIdForUpdate(@Param("walletId") String walletId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
		SELECT w
		FROM Wallet w
		WHERE w.user.id = :userId
		AND w.type = :type
		""")
    Optional<Wallet> findByUserIdAndTypeForUpdate(@Param("userId") String userId, @Param("type") WalletType type);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
	SELECT w FROM Wallet w
	WHERE w.type = :type
	""")
    List<Wallet> findByTypeForUpdate(@Param("type") WalletType type);
}
