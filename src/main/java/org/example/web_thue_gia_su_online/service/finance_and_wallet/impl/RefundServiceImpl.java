package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.disputing_and_reporting.DisputeRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
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

    @Override
    @Transactional
    public void refundDispute(Dispute dispute) {
        RefundInfo refundInfo = resolveRefundInfo(dispute);

        // Lấy và khóa hai Wallet để tránh hoàn tiền đồng thời
        Wallet tutorWallet = walletRepository
                .findByUser_IdForUpdate(refundInfo.tutorId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        Wallet studentWallet = walletRepository
                .findByUser_IdForUpdate(refundInfo.studentId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        // Kiểm tra tránh hoàn tiền hai lần
        if (disputeRepository.existsByIdAndStatus(
                dispute.getId(), DisputeStatus.APPROVED)) {
            throw new AppException(ErrorCode.REFUND_ALREADY_PROCESSED);
        }

        boolean escrowReleased = dispute.getBooking() != null
                ? paymentTransactionRepository.existsByBooking_IdAndTypeAndStatus(
                        dispute.getBooking().getId(), TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS)
                : (dispute.getEnrollment() != null
                        && paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatus(
                                dispute.getEnrollment().getId(),
                                TransactionType.ESCROW_RELEASE,
                                TransactionStatus.SUCCESS));

        if (escrowReleased) {
            // Tiền đã giải ngân cho Tutor -> Thu hồi tiền từ Tutor
            if (tutorWallet.getAvailableBalance() < refundInfo.amount()) {
                throw new AppException(ErrorCode.INSUFFICIENT_TUTOR_BALANCE);
            }
            tutorWallet.setAvailableBalance(tutorWallet.getAvailableBalance() - refundInfo.amount());
            walletRepository.save(tutorWallet);
        } else {
            // Tiền vẫn đang tạm giữ (holding balance) trong ví của student -> giải tỏa holding balance
            if (studentWallet.getHoldingBalance() >= refundInfo.amount()) {
                studentWallet.setHoldingBalance(studentWallet.getHoldingBalance() - refundInfo.amount());
            }
        }

        // Hoàn tiền cho Student
        studentWallet.setAvailableBalance(studentWallet.getAvailableBalance() + refundInfo.amount());
        walletRepository.save(studentWallet);

        // Ghi nhận giao dịch hoàn tiền
        PaymentTransaction transaction = PaymentTransaction.builder()
                .sourceWallet(tutorWallet)
                .destinationWallet(studentWallet)
                .type(TransactionType.REFUND)
                .status(TransactionStatus.SUCCESS)
                .referenceCode(java.util.UUID.randomUUID().toString())
                .amount(refundInfo.amount())
                .balanceAfter(studentWallet.getAvailableBalance())
                .booking(dispute.getBooking())
                .enrollment(dispute.getEnrollment())
                .note("Hoàn tiền khiếu nại thành công")
                .build();

        paymentTransactionRepository.save(transaction);
    }

    private RefundInfo resolveRefundInfo(Dispute dispute) {

        if (dispute.getBooking() != null && dispute.getEnrollment() != null)
            throw new AppException(ErrorCode.UNAUTHORIZED);

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
