package org.example.web_thue_gia_su_online.repository.finance_and_wallet;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, String> {
    Page<PaymentTransaction> findBySourceWalletId(String sourceWalletId, Pageable pageable);

    Page<PaymentTransaction> findByDestinationWalletId(String destinationWalletId, Pageable pageable);

    Page<PaymentTransaction> findByBookingId(String bookingId, Pageable pageable);

    Optional<PaymentTransaction> findByReferenceCode(String referenceCode);

    boolean existsByBooking_IdAndTypeAndStatus(String bookingId, TransactionType type, TransactionStatus status);

    boolean existsByEnrollment_IdAndTypeAndStatus(String enrollmentId, TransactionType type, TransactionStatus status);

    boolean existsByBooking_IdAndTypeAndStatusAndDestinationWallet_Id(
            String bookingId, TransactionType type, TransactionStatus status, String destinationWalletId);

    boolean existsByEnrollment_IdAndTypeAndStatusAndDestinationWallet_Id(
            String enrollmentId, TransactionType type, TransactionStatus status, String destinationWalletId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
	SELECT t FROM PaymentTransaction t
	WHERE t.enrollment.id = :enrollmentId
		AND t.type = :type
		AND t.status = :status
	""")
    List<PaymentTransaction> findEnrollmentHoldForUpdate(
            @Param("enrollmentId") String enrollmentId,
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
			SELECT pt
			FROM PaymentTransaction pt
			WHERE pt.id = :transactionId
			""")
    Optional<PaymentTransaction> findByIdForUpdate(@Param("transactionId") String transactionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
	SELECT t FROM PaymentTransaction t
	WHERE t.booking.id = :bookingId
	AND t.type = :type
	AND t.status = :status
	""")
    List<PaymentTransaction> findBookingHoldForUpdate(
            @Param("bookingId") String bookingId,
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status);

    Optional<PaymentTransaction> findByWithdrawal_Id(String withdrawalId);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse(
			pt.id,
			pt.sourceWallet.id,
			pt.destinationWallet.id,
			pt.booking.id,
			pt.type,
			pt.amount,
			pt.balanceAfter,
			pt.note,
			pt.createdAt
		)
		FROM PaymentTransaction pt
		WHERE pt.id = :id
		""")
    Optional<PaymentTransactionResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse(
			pt.id,
			pt.sourceWallet.id,
			pt.destinationWallet.id,
			pt.booking.id,
			pt.type,
			pt.amount,
			pt.balanceAfter,
			pt.note,
			pt.createdAt
		)
		FROM PaymentTransaction pt
		WHERE pt.booking.id = :bookingId
		""")
    Page<PaymentTransactionResponse> findResponsesByBookingId(@Param("bookingId") String bookingId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse(
			pt.id,
			pt.sourceWallet.id,
			pt.destinationWallet.id,
			pt.booking.id,
			pt.type,
			pt.amount,
			pt.balanceAfter,
			pt.note,
			pt.createdAt
		)
		FROM PaymentTransaction pt
		WHERE pt.sourceWallet.id = :walletId OR pt.destinationWallet.id = :walletId
		ORDER BY pt.createdAt DESC
		""")
    Page<PaymentTransactionResponse> findResponsesByWalletId(@Param("walletId") String walletId, Pageable pageable);
}
