package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.PaymentTransactionMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.OrderRepository;
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
    OrderRepository orderRepository;
    PaymentTransactionMapper paymentTransactionMapper;

    @Override
    @Transactional
    public PaymentTransactionResponse createTransaction(
            PaymentTransactionCreationRequest request, String walletId, String orderId) {

        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));

        Order order = null;
        if (orderId != null && !orderId.isBlank()) {
            order = orderRepository.findById(orderId).orElse(null);
        }

        PaymentTransaction transaction = paymentTransactionMapper.toPaymentTransaction(request);
        transaction.setWallet(wallet);
        transaction.setOrder(order);
        transaction.setCreatedAt(Instant.now());
        transaction.setUpdateAt(Instant.now());

        // Cập nhật số dư ví tùy theo loại giao dịch
        switch (request.getType()) {
            case DEPOSIT, SESSION_PAYOUT, REFUND -> wallet.setBalance(wallet.getBalance() + request.getAmount());
            case ESCROW_HOLD, BROKERAGE_FEE, PLATFORM_CUT, WITHDRAWAL ->
                    wallet.setBalance(wallet.getBalance() - request.getAmount());
        }
        wallet.setUpdateAt(Instant.now());
        walletRepository.save(wallet);

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
        return paymentTransactionRepository.findByWalletId(walletId).stream()
                .map(paymentTransactionMapper::toPaymentTransactionResponse)
                .toList();
    }

    @Override
    public List<PaymentTransactionResponse> getTransactionsByOrder(String orderId) {
        return paymentTransactionRepository.findByOrderId(orderId).stream()
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