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

        // Kiểm tra Tutor còn đủ tiền không
        if (tutorWallet.getAvailableBalance() < refundInfo.amount()) {
            throw new AppException(ErrorCode.INSUFFICIENT_TUTOR_BALANCE);
        }

        // Kiểm tra tránh hoàn tiền hai lần
        if (disputeRepository.existsSuccessfulRefund(dispute.getId(), DisputeStatus.APPROVED)) {
            throw new AppException(ErrorCode.REFUND_ALREADY_PROCESSED);
        }

        // Thu hồi tiền từ Tutor
        tutorWallet.setAvailableBalance(tutorWallet.getAvailableBalance() - refundInfo.amount());

        // Hoàn tiền cho Student
        studentWallet.setAvailableBalance(studentWallet.getAvailableBalance() + refundInfo.amount());

        // Ghi nhận giao dịch hoàn tiền
        PaymentTransaction transaction = PaymentTransaction.builder()
                .type(TransactionType.REFUND)
                .status(TransactionStatus.SUCCESS)
                .amount(refundInfo.amount())
                .booking(dispute.getBooking())
                .enrollment(dispute.getEnrollment())
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
