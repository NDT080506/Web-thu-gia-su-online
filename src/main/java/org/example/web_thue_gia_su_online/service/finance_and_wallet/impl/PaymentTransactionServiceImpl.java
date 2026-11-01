package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.PaymentTransactionMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentTransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    PaymentTransactionRepository paymentTransactionRepository;
    WalletRepository walletRepository;
    BookingRepository bookingRepository;
    PaymentTransactionMapper paymentTransactionMapper;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    private boolean isAdmin() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ADMIN") || a.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Kiểm tra transaction có thuộc về current user không.
     * Một transaction thuộc về user nếu source wallet hoặc destination wallet thuộc về user.
     */
    private boolean isTransactionOwner(PaymentTransaction transaction) {
        String currentUserId = getCurrentUserId();
        if (transaction.getSourceWallet() != null
                && transaction.getSourceWallet().getUser().getId().equals(currentUserId)) {
            return true;
        }
        if (transaction.getDestinationWallet() != null
                && transaction.getDestinationWallet().getUser().getId().equals(currentUserId)) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentTransactionResponse createTransaction(PaymentTransactionCreationRequest request) {
        // Chỉ admin mới có thể tạo transaction thủ công.
        // Student/Tutor KHÔNG được tự tạo transaction làm thay đổi balance.
        // Balance chỉ được thay đổi qua các use case domain (payForBooking, payForEnrollment, escrow release, deposit).

        Wallet sourceWallet = null;
        if (request.getSourceWalletId() != null && !request.getSourceWalletId().isBlank()) {
            sourceWallet = walletRepository
                    .findById(request.getSourceWalletId())
                    .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

            // Kiểm tra số dư đủ
            if (sourceWallet.getAvailableBalance() < request.getAmount()) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            sourceWallet.setAvailableBalance(
                    Math.subtractExact(sourceWallet.getAvailableBalance(), request.getAmount()));
            sourceWallet.setUpdatedAt(Instant.now());
            walletRepository.save(sourceWallet);
        }

        Wallet destinationWallet = null;
        if (request.getDestinationWalletId() != null
                && !request.getDestinationWalletId().isBlank()) {
            destinationWallet = walletRepository
                    .findById(request.getDestinationWalletId())
                    .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
            destinationWallet.setAvailableBalance(
                    Math.addExact(destinationWallet.getAvailableBalance(), request.getAmount()));
            destinationWallet.setUpdatedAt(Instant.now());
            walletRepository.save(destinationWallet);
        }

        Booking booking = null;
        if (request.getBookingId() != null && !request.getBookingId().isBlank()) {
            booking = bookingRepository.findById(request.getBookingId()).orElse(null);
        }

        long balanceAfter = request.getBalanceAfter() != null
                ? request.getBalanceAfter()
                : (sourceWallet != null ? sourceWallet.getAvailableBalance() : 0);

        PaymentTransaction transaction = PaymentTransaction.builder()
                .sourceWallet(sourceWallet)
                .destinationWallet(destinationWallet)
                .booking(booking)
                .type(request.getType())
                .amount(request.getAmount())
                .balanceAfter(balanceAfter)
                .note(request.getNote())
                .createdAt(Instant.now())
                .build();

        return paymentTransactionMapper.toPaymentTransactionResponse(paymentTransactionRepository.save(transaction));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TUTOR')")
    public PaymentTransactionResponse getTransaction(String id) {
        PaymentTransaction transaction = paymentTransactionRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        // Authorization: chỉ owner của source/destination wallet hoặc admin mới được xem
        if (!isAdmin() && !isTransactionOwner(transaction)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return paymentTransactionMapper.toPaymentTransactionResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TUTOR')")
    public List<PaymentTransactionResponse> getTransactionsByWallet(String walletId) {
        // Authorization: chỉ owner của wallet hoặc admin mới được xem
        Wallet wallet =
                walletRepository.findById(walletId).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
        if (!isAdmin() && !wallet.getUser().getId().equals(getCurrentUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        List<PaymentTransaction> results = new ArrayList<>();
        results.addAll(paymentTransactionRepository.findBySourceWalletId(walletId));
        results.addAll(paymentTransactionRepository.findByDestinationWalletId(walletId));
        return results.stream()
                .distinct()
                .map(paymentTransactionMapper::toPaymentTransactionResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<PaymentTransactionResponse> getTransactionsByBooking(String bookingId) {
        return paymentTransactionRepository.findByBookingId(bookingId).stream()
                .map(paymentTransactionMapper::toPaymentTransactionResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentTransactionResponse> getTransactions() {
        return paymentTransactionRepository.findAll().stream()
                .map(paymentTransactionMapper::toPaymentTransactionResponse)
                .toList();
    }
}
