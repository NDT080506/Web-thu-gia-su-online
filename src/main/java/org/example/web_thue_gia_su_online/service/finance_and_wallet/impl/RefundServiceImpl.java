package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.util.List;
import java.util.UUID;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.BookingStatus;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;
import org.example.web_thue_gia_su_online.enums.EnrollmentStatus;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.disputing_and_reporting.DisputeRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.RefundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefundServiceImpl implements RefundService {

    WalletRepository walletRepository;
    PaymentTransactionRepository paymentTransactionRepository;
    DisputeRepository disputeRepository;
    BookingRepository bookingRepository;
    CourseEnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public void refundDispute(Dispute dispute) {
        RefundInfo refundInfo = resolveRefundInfo(dispute);

        // Kiểm tra tránh hoàn tiền hai lần
        if (disputeRepository.existsSuccessfulRefund(dispute.getId(), DisputeStatus.APPROVED)) {
            throw new AppException(ErrorCode.REFUND_ALREADY_PROCESSED);
        }

        Wallet studentWallet = walletRepository
                .findByUser_IdForUpdate(refundInfo.studentId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        long amount = refundInfo.amount();
        String bookingId = dispute.getBooking() != null ? dispute.getBooking().getId() : null;
        String enrollmentId =
                dispute.getEnrollment() != null ? dispute.getEnrollment().getId() : null;

        // Kiểm tra trạng thái giao dịch tiền:
        // Case 1: Tiền vẫn đang trong ESCROW_HOLD (chưa giải ngân cho gia sư)
        boolean hasHold = false;
        if (bookingId != null) {
            hasHold = paymentTransactionRepository.existsByBooking_IdAndTypeAndStatus(
                    bookingId, TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);
        } else if (enrollmentId != null) {
            hasHold = paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatus(
                    enrollmentId, TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);
        }

        boolean hasRelease = false;
        if (bookingId != null) {
            hasRelease = paymentTransactionRepository.existsByBooking_IdAndTypeAndStatus(
                    bookingId, TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS);
        } else if (enrollmentId != null) {
            hasRelease = paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatus(
                    enrollmentId, TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS);
        }

        if (hasHold && !hasRelease) {
            // Tiền đang giữ trong holdingBalance của học sinh -> giải tỏa holding, hoàn lại availableBalance
            if (studentWallet.getHoldingBalance() >= amount) {
                studentWallet.setHoldingBalance(Math.subtractExact(studentWallet.getHoldingBalance(), amount));
            }
            studentWallet.setAvailableBalance(Math.addExact(studentWallet.getAvailableBalance(), amount));
            walletRepository.save(studentWallet);
        } else {
            // Tiền đã giải ngân -> thu hồi từ ví gia sư hoặc ví nền tảng, hoàn về ví học sinh
            Wallet tutorWallet = walletRepository
                    .findByUser_IdForUpdate(refundInfo.tutorId())
                    .orElse(null);

            if (tutorWallet != null && tutorWallet.getAvailableBalance() >= amount) {
                tutorWallet.setAvailableBalance(Math.subtractExact(tutorWallet.getAvailableBalance(), amount));
                walletRepository.save(tutorWallet);
            } else {
                List<Wallet> platformWallets = walletRepository.findByTypeForUpdate(WalletType.PLATFORM);
                if (!platformWallets.isEmpty()) {
                    Wallet platformWallet = platformWallets.getFirst();
                    if (platformWallet.getAvailableBalance() >= amount) {
                        platformWallet.setAvailableBalance(
                                Math.subtractExact(platformWallet.getAvailableBalance(), amount));
                        walletRepository.save(platformWallet);
                    }
                }
            }

            studentWallet.setAvailableBalance(Math.addExact(studentWallet.getAvailableBalance(), amount));
            walletRepository.save(studentWallet);
        }

        // Cập nhật trạng thái Booking/Enrollment sang CANCELLED
        if (dispute.getBooking() != null) {
            Booking booking = dispute.getBooking();
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
        }

        if (dispute.getEnrollment() != null) {
            CourseEnrollment enrollment = dispute.getEnrollment();
            enrollment.setStatus(EnrollmentStatus.CANCELLED);
            enrollmentRepository.save(enrollment);
        }

        // Ghi nhận giao dịch hoàn tiền
        PaymentTransaction transaction = PaymentTransaction.builder()
                .destinationWallet(studentWallet)
                .type(TransactionType.REFUND)
                .status(TransactionStatus.SUCCESS)
                .amount(amount)
                .referenceCode(UUID.randomUUID().toString())
                .balanceAfter(studentWallet.getAvailableBalance())
                .note("Hoàn tiền cho khiếu nại #" + dispute.getId())
                .booking(dispute.getBooking())
                .enrollment(dispute.getEnrollment())
                .build();

        paymentTransactionRepository.save(transaction);
    }

    private RefundInfo resolveRefundInfo(Dispute dispute) {
        if (dispute.getBooking() != null && dispute.getEnrollment() != null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (dispute.getBooking() != null) {
            Booking booking = dispute.getBooking();
            return new RefundInfo(
                    booking.getStudent().getId(),
                    booking.getBrokeragePackage().getTutor().getId(),
                    booking.getTotalPrice());
        }

        CourseEnrollment enrollment = dispute.getEnrollment();
        return new RefundInfo(
                enrollment.getStudent().getId(),
                enrollment.getCourse().getTutor().getId(),
                enrollment.getTotalPrice());
    }

    private record RefundInfo(String studentId, String tutorId, Long amount) {}
}
