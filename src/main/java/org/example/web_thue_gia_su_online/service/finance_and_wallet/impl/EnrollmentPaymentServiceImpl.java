package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.util.Objects;
import java.util.UUID;

import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.EnrollmentPaymentResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.EnrollmentStatus;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.EnrollmentPaymentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentPaymentServiceImpl implements EnrollmentPaymentService {

    CourseEnrollmentRepository courseEnrollmentRepository;
    WalletRepository walletRepository;
    PaymentTransactionRepository paymentTransactionRepository;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    public EnrollmentPaymentResponse payForEnrollment(String enrollmentId) {
        String userId = getCurrentUserId();

        CourseEnrollment enrollment = courseEnrollmentRepository
                .findByIdForUpdate(enrollmentId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));

        if (enrollment.getStudent() == null
                || !userId.equals(enrollment.getStudent().getId())) {
            throw new IllegalArgumentException("Bạn không có quyền thanh toán booking này");
        }

        long amount = enrollment.getTotalPrice();

        if (amount <= 0) {
            throw new IllegalStateException("Số tiền thanh toán không hợp lệ");
        }

        boolean alreadyHeld = paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatus(
                enrollmentId, TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);

        if (alreadyHeld) {
            throw new IllegalStateException("Booking này đã được thanh toán");
        }

        Wallet wallet = walletRepository
                .findByUserIdAndTypeForUpdate(userId, WalletType.NORMAL)
                .orElseThrow(() -> new IllegalStateException("Bạn chưa có ví thanh toán"));

        if (wallet.getAvailableBalance() < amount) {
            throw new IllegalStateException("Số dư khả dụng không đủ để thanh toán");
        }

        wallet.setAvailableBalance(wallet.getAvailableBalance() - amount);

        wallet.setHoldingBalance(Math.addExact(wallet.getHoldingBalance(), amount));

        PaymentTransaction transaction = PaymentTransaction.builder()
                .sourceWallet(wallet)
                .destinationWallet(null)
                .enrollment(enrollment)
                .type(TransactionType.ESCROW_HOLD)
                .status(TransactionStatus.SUCCESS)
                .referenceCode(UUID.randomUUID().toString())
                .amount(amount)
                .balanceAfter(wallet.getAvailableBalance())
                .note("Giữ tiền thanh toán cho enrollment " + enrollmentId)
                .build();

        PaymentTransaction savedTransaction = paymentTransactionRepository.save(transaction);

        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        courseEnrollmentRepository.save(enrollment);

        return new EnrollmentPaymentResponse(
                savedTransaction.getId(),
                enrollment.getId(),
                savedTransaction.getReferenceCode(),
                amount,
                wallet.getAvailableBalance(),
                wallet.getHoldingBalance(),
                savedTransaction.getType(),
                savedTransaction.getStatus(),
                savedTransaction.getCreatedAt());
    }
}
