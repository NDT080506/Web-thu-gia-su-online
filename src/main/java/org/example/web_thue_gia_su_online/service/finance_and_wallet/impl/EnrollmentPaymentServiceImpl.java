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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public EnrollmentPaymentResponse payForEnrollment(String enrollmentId) {
        String userId = getCurrentUserId();

        // 1. Tìm và khóa enrollment
        CourseEnrollment enrollment = courseEnrollmentRepository
                .findByIdForUpdate(enrollmentId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));

        // 2. Chỉ student sở hữu enrollment mới được thanh toán
        if (enrollment.getStudent() == null
                || !userId.equals(enrollment.getStudent().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 3. Chỉ enrollment ở trạng thái PENDING mới được thanh toán
        if (enrollment.getStatus() != EnrollmentStatus.PENDING) {
            throw new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_ACTIVE);
        }

        // 4. Kiểm tra số tiền
        long amount = enrollment.getTotalPrice();

        if (amount <= 0) {
            throw new IllegalStateException("Số tiền thanh toán không hợp lệ");
        }

        // 5. Kiểm tra đã có escrow hold chưa (idempotency)
        boolean alreadyHeld = paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatus(
                enrollmentId, TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);

        if (alreadyHeld) {
            throw new IllegalStateException("Enrollment này đã được thanh toán");
        }

        // 6. Tìm và khóa ví của student
        Wallet wallet = walletRepository
                .findByUserIdAndTypeForUpdate(userId, WalletType.NORMAL)
                .orElseThrow(() -> new IllegalStateException("Bạn chưa có ví thanh toán"));

        // 7. Kiểm tra số dư khả dụng
        if (wallet.getAvailableBalance() < amount) {
            throw new IllegalStateException("Số dư khả dụng không đủ để thanh toán");
        }

        // 8. Chuyển tiền từ available sang holding (chống overflow)
        wallet.setAvailableBalance(Math.subtractExact(wallet.getAvailableBalance(), amount));
        wallet.setHoldingBalance(Math.addExact(wallet.getHoldingBalance(), amount));

        // 9. Tạo giao dịch escrow hold
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

        // 10. Cập nhật enrollment sang ACTIVE (đã thanh toán)
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        courseEnrollmentRepository.save(enrollment);

        // 11. Lưu wallet (bắt buộc để persist thay đổi balance)
        walletRepository.save(wallet);

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
