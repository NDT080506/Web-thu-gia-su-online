package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.disputing_and_reporting.DisputeStatus;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.PaymentGatewayType;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.TransactionType;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.WalletType;
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
    public boolean refundDispute(Dispute dispute) {
        RefundInfo refundInfo = resolveRefundInfo(dispute);

        if (disputeRepository.existsByIdAndStatus(dispute.getId(), DisputeStatus.APPROVED)) {
            throw new AppException(ErrorCode.REFUND_ALREADY_PROCESSED);
        }

        List<PaymentTransaction> pending =
                findTransactionsForUpdate(refundInfo, TransactionType.REFUND, TransactionStatus.PENDING);
        if (!pending.isEmpty()) {
            return processPendingRefunds(pending);
        }

        if (!findTransactionsForUpdate(refundInfo, TransactionType.REFUND, TransactionStatus.SUCCESS)
                .isEmpty()) {
            throw new AppException(ErrorCode.REFUND_ALREADY_PROCESSED);
        }

        List<PaymentTransaction> holds =
                findTransactionsForUpdate(refundInfo, TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);
        if (holds.size() != 1 || holds.getFirst().getSourceWallet() == null) {
            throw new IllegalStateException("Phải có đúng một giao dịch giữ tiền hợp lệ trước khi hoàn tiền");
        }

        Wallet studentWallet = holds.getFirst().getSourceWallet();
        if (!refundInfo.studentId().equals(studentWallet.getUser().getId())) {
            throw new IllegalStateException("Ví giữ tiền không thuộc về học viên của giao dịch");
        }

        List<PaymentTransaction> releases =
                findTransactionsForUpdate(refundInfo, TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS);
        if (releases.isEmpty()) {
            PaymentTransaction refund =
                    buildRefundTransaction(studentWallet, studentWallet, refundInfo, refundInfo.amount());
            return processPendingRefunds(paymentTransactionRepository.saveAll(List.of(refund)));
        }

        Wallet tutorWallet = walletRepository
                .findByUserIdAndTypeForUpdate(refundInfo.tutorId(), WalletType.NORMAL)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        List<Wallet> platformWallets = walletRepository.findByTypeForUpdate(WalletType.PLATFORM);
        if (platformWallets.size() != 1) {
            throw new IllegalStateException("Hệ thống phải có đúng một ví nền tảng");
        }
        Wallet platformWallet = platformWallets.getFirst();
        studentWallet = walletRepository
                .findByIdForUpdate(studentWallet.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        long tutorReleased = 0;
        long platformReleased = 0;
        for (PaymentTransaction release : releases) {
            if (release.getSourceWallet() == null
                    || release.getDestinationWallet() == null
                    || !studentWallet.getId().equals(release.getSourceWallet().getId())) {
                throw new IllegalStateException("Giao dịch giải ngân không hợp lệ");
            }
            if (tutorWallet.getId().equals(release.getDestinationWallet().getId())) {
                tutorReleased = Math.addExact(tutorReleased, release.getAmount());
            } else if (platformWallet
                    .getId()
                    .equals(release.getDestinationWallet().getId())) {
                platformReleased = Math.addExact(platformReleased, release.getAmount());
            } else {
                throw new IllegalStateException("Giao dịch giải ngân có ví đích không hợp lệ");
            }
        }
        if (tutorReleased != refundInfo.tutorIncome()
                || platformReleased != refundInfo.platformFee()
                || Math.addExact(tutorReleased, platformReleased) != refundInfo.amount()) {
            throw new IllegalStateException("Số tiền đã giải ngân không khớp với giá trị cần hoàn");
        }

        List<PaymentTransaction> refunds = new ArrayList<>();
        if (tutorReleased > 0) {
            refunds.add(buildRefundTransaction(tutorWallet, studentWallet, refundInfo, tutorReleased));
        }
        if (platformReleased > 0) {
            refunds.add(buildRefundTransaction(platformWallet, studentWallet, refundInfo, platformReleased));
        }
        return processPendingRefunds(paymentTransactionRepository.saveAll(refunds));
    }

    private PaymentTransaction buildRefundTransaction(
            Wallet source, Wallet destination, RefundInfo refundInfo, long amount) {
        return PaymentTransaction.builder()
                .sourceWallet(source)
                .destinationWallet(destination)
                .type(TransactionType.REFUND)
                .status(TransactionStatus.PENDING)
                .provider(PaymentGatewayType.INTERNAL)
                .referenceCode(UUID.randomUUID().toString())
                .amount(amount)
                .booking(refundInfo.booking())
                .enrollment(refundInfo.enrollment())
                .note("Hoàn tiền cho khiếu nại " + refundInfo.disputeId())
                .build();
    }

    private boolean processPendingRefunds(List<PaymentTransaction> refunds) {
        Map<String, Wallet> wallets = new HashMap<>();
        refunds.stream()
                .flatMap(transaction ->
                        java.util.stream.Stream.of(transaction.getSourceWallet(), transaction.getDestinationWallet()))
                .filter(wallet -> wallet != null)
                .map(Wallet::getId)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .forEach(walletId -> wallets.put(
                        walletId,
                        walletRepository
                                .findByIdForUpdate(walletId)
                                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND))));

        Map<String, Long> availableDebits = new HashMap<>();
        Map<String, Long> holdingDebits = new HashMap<>();
        Map<String, Long> credits = new HashMap<>();
        for (PaymentTransaction refund : refunds) {
            Wallet source = wallets.get(refund.getSourceWallet().getId());
            Wallet destination = wallets.get(refund.getDestinationWallet().getId());
            refund.setSourceWallet(source);
            refund.setDestinationWallet(destination);
            if (refund.getAmount() <= 0) {
                throw new IllegalStateException("Số tiền hoàn phải lớn hơn 0");
            }

            Map<String, Long> debits = source.getId().equals(destination.getId()) ? holdingDebits : availableDebits;
            debits.merge(source.getId(), refund.getAmount(), Math::addExact);
            credits.merge(destination.getId(), refund.getAmount(), Math::addExact);
        }

        for (Map.Entry<String, Long> debit : availableDebits.entrySet()) {
            if (wallets.get(debit.getKey()).getAvailableBalance() < debit.getValue()) {
                return false;
            }
        }
        for (Map.Entry<String, Long> debit : holdingDebits.entrySet()) {
            if (wallets.get(debit.getKey()).getHoldingBalance() < debit.getValue()) {
                return false;
            }
        }
        credits.forEach(
                (walletId, amount) -> Math.addExact(wallets.get(walletId).getAvailableBalance(), amount));

        availableDebits.forEach((walletId, amount) -> {
            Wallet wallet = wallets.get(walletId);
            wallet.setAvailableBalance(Math.subtractExact(wallet.getAvailableBalance(), amount));
        });
        holdingDebits.forEach((walletId, amount) -> {
            Wallet wallet = wallets.get(walletId);
            wallet.setHoldingBalance(Math.subtractExact(wallet.getHoldingBalance(), amount));
        });
        credits.forEach((walletId, amount) -> {
            Wallet wallet = wallets.get(walletId);
            wallet.setAvailableBalance(Math.addExact(wallet.getAvailableBalance(), amount));
            walletRepository.save(wallet);
        });

        for (PaymentTransaction refund : refunds) {
            refund.setStatus(TransactionStatus.SUCCESS);
            refund.setBalanceAfter(refund.getDestinationWallet().getAvailableBalance());
        }
        paymentTransactionRepository.saveAll(refunds);
        return true;
    }

    private List<PaymentTransaction> findTransactionsForUpdate(
            RefundInfo refundInfo, TransactionType type, TransactionStatus status) {
        return refundInfo.booking() != null
                ? paymentTransactionRepository.findBookingTransactionsForUpdate(
                        refundInfo.booking().getId(), type, status)
                : paymentTransactionRepository.findEnrollmentTransactionsForUpdate(
                        refundInfo.enrollment().getId(), type, status);
    }

    private RefundInfo resolveRefundInfo(Dispute dispute) {

        if ((dispute.getBooking() == null) == (dispute.getEnrollment() == null))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        if (dispute.getBooking() != null) {
            Booking booking = dispute.getBooking();

            return new RefundInfo(
                    dispute.getId(),
                    booking.getStudent().getId(),
                    booking.getBrokeragePackage().getTutor().getId(),
                    resolveAmount(dispute, booking.getTotalPrice()),
                    booking.getTutorIncome(),
                    booking.getPlatformFee(),
                    booking,
                    null);
        }

        CourseEnrollment enrollment = dispute.getEnrollment();
        return new RefundInfo(
                dispute.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getTutor().getId(),
                resolveAmount(dispute, enrollment.getTotalPrice()),
                enrollment.getTutorIncome(),
                enrollment.getPlatformFee(),
                null,
                enrollment);
    }

    private long resolveAmount(Dispute dispute, long totalPrice) {
        long amount = dispute.getRefundAmount() == 0 ? totalPrice : dispute.getRefundAmount();
        if (totalPrice <= 0 || amount != totalPrice) {
            throw new IllegalStateException("Hiện chỉ hỗ trợ hoàn toàn bộ số tiền đã thanh toán");
        }
        return amount;
    }

    private record RefundInfo(
            String disputeId,
            String studentId,
            String tutorId,
            long amount,
            long tutorIncome,
            long platformFee,
            Booking booking,
            CourseEnrollment enrollment) {}
}
