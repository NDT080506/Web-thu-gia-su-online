package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.time.Instant;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WithdrawalRequest;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PayoutResult;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Withdrawal;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.example.web_thue_gia_su_online.enums.WithdrawalStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WithdrawalRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WithdrawalPersistenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawalPersistenceServiceImpl implements WithdrawalPersistenceService {

    WalletRepository walletRepository;
    WithdrawalRepository withdrawalRepository;
    PaymentTransactionRepository transactionRepository;

    @Override
    @Transactional
    public Withdrawal reserve(String userId, WithdrawalRequest request, String referenceCode) {
        Wallet wallet = walletRepository
                .findByUserIdAndTypeForUpdate(userId, WalletType.NORMAL)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        long amount = request.getAmount();

        if (wallet.getAvailableBalance() < amount) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        wallet.setAvailableBalance(Math.subtractExact(wallet.getAvailableBalance(), amount));

        Withdrawal withdrawal = Withdrawal.builder()
                .user(wallet.getUser())
                .wallet(wallet)
                .amount(amount)
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .accountHolder(request.getAccountHolder())
                .status(WithdrawalStatus.PROCESSING)
                .build();

        withdrawalRepository.save(withdrawal);

        PaymentTransaction transaction = PaymentTransaction.builder()
                .sourceWallet(wallet)
                .withdrawal(withdrawal)
                .type(TransactionType.WITHDRAW)
                .status(TransactionStatus.PENDING)
                .amount(amount)
                .balanceAfter(wallet.getAvailableBalance())
                .referenceCode(referenceCode)
                .note("Withdrawal: " + withdrawal.getId())
                .build();

        transactionRepository.save(transaction);
        walletRepository.save(wallet);

        return withdrawal;
    }

    @Override
    @Transactional
    public void complete(String withdrawalId, PayoutResult result) {
        Withdrawal withdrawal = withdrawalRepository
                .findByIdForUpdate(withdrawalId)
                .orElseThrow(() -> new AppException(ErrorCode.WITHDRAWAL_NOT_FOUND));

        PaymentTransaction transaction = transactionRepository
                .findByWithdrawal_Id(withdrawalId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        // Chống cập nhật kết quả lần thứ hai
        if (withdrawal.getStatus() != WithdrawalStatus.PROCESSING
                || transaction.getStatus() != TransactionStatus.PENDING) {
            return;
        }

        withdrawal.setProviderReference(result.providerReference());
        withdrawal.setProcessedAt(Instant.now());

        if (result.successful()) {
            withdrawal.setStatus(WithdrawalStatus.SUCCESS);
            transaction.setStatus(TransactionStatus.SUCCESS);
        } else {
            refund(withdrawal);

            withdrawal.setStatus(WithdrawalStatus.FAILED);
            withdrawal.setFailureReason(result.failureReason());
            transaction.setStatus(TransactionStatus.FAILED);
        }

        withdrawalRepository.save(withdrawal);
        transactionRepository.save(transaction);
    }

    private void refund(Withdrawal withdrawal) {
        Wallet wallet = walletRepository
                .findByIdForUpdate(withdrawal.getWallet().getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        wallet.setAvailableBalance(Math.addExact(wallet.getAvailableBalance(), withdrawal.getAmount()));

        walletRepository.save(wallet);
    }
}
