package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.PaymentTransactionMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentTransactionService;
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

    @Override
    @Transactional
    public PaymentTransactionResponse createTransaction(PaymentTransactionCreationRequest request) {
        Wallet sourceWallet = null;
        if (request.getSourceWalletId() != null && !request.getSourceWalletId().isBlank()) {
            sourceWallet = walletRepository
                    .findById(request.getSourceWalletId())
                    .orElseThrow(() -> new RuntimeException("Source wallet not found"));
            sourceWallet.setAvailableBalance(sourceWallet.getAvailableBalance() - request.getAmount());
            sourceWallet.setUpdatedAt(Instant.now());
            walletRepository.save(sourceWallet);
        }

        Wallet destinationWallet = null;
        if (request.getDestinationWalletId() != null
                && !request.getDestinationWalletId().isBlank()) {
            destinationWallet = walletRepository
                    .findById(request.getDestinationWalletId())
                    .orElseThrow(() -> new RuntimeException("Destination wallet not found"));
            destinationWallet.setAvailableBalance(destinationWallet.getAvailableBalance() + request.getAmount());
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
    public PaymentTransactionResponse getTransaction(String id) {
        PaymentTransaction transaction = paymentTransactionRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        return paymentTransactionMapper.toPaymentTransactionResponse(transaction);
    }

    @Override
    public List<PaymentTransactionResponse> getTransactionsByWallet(String walletId) {
        List<PaymentTransaction> results = new ArrayList<>();
        results.addAll(paymentTransactionRepository.findBySourceWalletId(walletId));
        results.addAll(paymentTransactionRepository.findByDestinationWalletId(walletId));
        return results.stream()
                .distinct()
                .map(paymentTransactionMapper::toPaymentTransactionResponse)
                .toList();
    }

    @Override
    public List<PaymentTransactionResponse> getTransactionsByBooking(String bookingId) {
        return paymentTransactionRepository.findByBookingId(bookingId).stream()
                .map(paymentTransactionMapper::toPaymentTransactionResponse)
                .toList();
    }

    @Override
    public List<PaymentTransactionResponse> getTransactions() {
        return paymentTransactionRepository.findAll().stream()
                .map(paymentTransactionMapper::toPaymentTransactionResponse)
                .toList();
    }
}