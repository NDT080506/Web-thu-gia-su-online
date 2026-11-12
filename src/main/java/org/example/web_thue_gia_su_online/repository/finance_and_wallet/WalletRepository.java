package org.example.web_thue_gia_su_online.repository.finance_and_wallet;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.WalletType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    @Query("SELECT w FROM Wallet w WHERE w.user.id = :userId AND w.type = :type")
    Optional<Wallet> findByUser_IdAndType(@Param("userId") String userId, @Param("type") WalletType type);

    @Query("SELECT count(w) > 0 FROM Wallet w WHERE w.user.id = :userId AND w.type = :type")
    Boolean existsByUser_IdAndType(@Param("userId") String userId, @Param("type") WalletType type);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.user.id = :userId")
    Optional<Wallet> findByUser_IdForUpdate(@Param("userId") String userId);

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
	SELECT w
	FROM Wallet w
	WHERE w.type = :type
	""")
    List<Wallet> findByTypeForUpdate(@Param("type") WalletType type);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse(
			w.id,
			w.user.id,
			w.type,
			w.availableBalance,
			w.holdingBalance,
			w.createdAt,
			w.updatedAt
		)
		FROM Wallet w
		WHERE w.id = :id
		""")
    Optional<WalletResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse(
			w.id,
			w.user.id,
			w.type,
			w.availableBalance,
			w.holdingBalance,
			w.createdAt,
			w.updatedAt
		)
		FROM Wallet w
		WHERE w.user.id = :userId
		""")
    Page<WalletResponse> findResponsesByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse(
			w.id,
			w.user.id,
			w.type,
			w.availableBalance,
			w.holdingBalance,
			w.createdAt,
			w.updatedAt
		)
		FROM Wallet w
		""")
    Page<WalletResponse> findResponses(Pageable pageable);
}
