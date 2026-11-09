package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.time.Instant;
import java.util.UUID;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.DepositRequest;
import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentInitiationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.DepositResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentInitiationResult;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentVerificationResult;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.ActionCode;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.DepositService;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentGateway;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepositServiceImpl implements DepositService {

    WalletRepository walletRepository;
    PaymentTransactionRepository transactionRepository;
    PaymentGateway paymentGateway;
    ActionLogService actionLogService;
    UserRepository userRepository;

    @NonFinal
    @Value("${app.frontend-url}")
    String frontendUrl;

    @Override
    @Transactional
    public DepositResponse createDeposit(String userId, DepositRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0");
        }

        Wallet wallet = walletRepository
                .findByUser_IdAndType(userId, WalletType.NORMAL)
                .or(() -> walletRepository.findByUserId(userId))
                .orElseGet(() -> {
                    User user = userRepository
                            .findById(userId)
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                    Instant now = Instant.now();
                    Wallet newWallet = Wallet.builder()
                            .user(user)
                            .type(WalletType.NORMAL)
                            .availableBalance(0)
                            .holdingBalance(0)
                            .createdAt(now)
                            .updatedAt(now)
                            .build();
                    return walletRepository.save(newWallet);
                });

        String referenceCode = UUID.randomUUID().toString();

        PaymentTransaction transaction = PaymentTransaction.builder()
                .destinationWallet(wallet)
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.PENDING)
                .provider(paymentGateway.getType())
                .referenceCode(referenceCode)
                .amount(request.getAmount())
                .balanceAfter(wallet.getAvailableBalance())
                .note("Nạp tiền vào ví")
                .build();

        transaction = transactionRepository.save(transaction);

        // Lấy user từ wallet để log
        actionLogService.log(ActionCode.DEPOSIT_MONEY, wallet.getUser(), "PaymentTransaction", transaction.getId());

        PaymentInitiationRequest gatewayRequest = PaymentInitiationRequest.builder()
                .transactionId(transaction.getId())
                .referenceCode(referenceCode)
                .amount(request.getAmount())
                .orderInfo("Nạp tiền vào ví")
                .returnUrl(frontendUrl + "/wallet/deposit/result")
                .build();

        PaymentInitiationResult result = paymentGateway.initiatePayment(gatewayRequest);

        transaction.setProviderReference(result.getProviderReference());
        transactionRepository.save(transaction);

        return toResponse(transaction, result.getPaymentUrl());
    }

    @Override
    @Transactional
    public DepositResponse confirmDeposit(String userId, String referenceCode) {
        PaymentTransaction existing = transactionRepository
                .findByReferenceCode(referenceCode)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giao dịch"));

        PaymentTransaction transaction = transactionRepository
                .findByIdForUpdate(existing.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giao dịch"));

        if (!transaction.getDestinationWallet().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Bạn không có quyền truy cập giao dịch này");
        }

        if (transaction.getStatus() == TransactionStatus.SUCCESS) {
            return toResponse(transaction, null);
        }

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalStateException("Giao dịch không còn ở trạng thái chờ thanh toán");
        }

        if (transaction.getType() != TransactionType.DEPOSIT) {
            throw new IllegalArgumentException("Đây không phải giao dịch nạp tiền");
        }

        if (transaction.getProvider() != paymentGateway.getType()) {
            throw new IllegalStateException("Gateway không khớp với giao dịch");
        }

        PaymentVerificationResult verification = paymentGateway.verifyPayment(referenceCode);

        if (!verification.isVerified()) {
            throw new IllegalStateException("Chưa xác minh được kết quả thanh toán");
        }

        if (!referenceCode.equals(verification.getReferenceCode())
                || verification.getAmount() == null
                || !verification.getAmount().equals(transaction.getAmount())) {
            throw new IllegalStateException("Thông tin thanh toán không khớp với giao dịch");
        }

        if (!verification.isSuccessful()) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            return toResponse(transaction, null);
        }

        if (transaction.getProviderReference() != null
                && !transaction.getProviderReference().equals(verification.getProviderReference())) {
            throw new IllegalStateException("Mã giao dịch từ gateway không khớp");
        }

        Wallet wallet = walletRepository
                .findByIdForUpdate(transaction.getDestinationWallet().getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ví"));

        long amount = transaction.getAmount();
        long currentBalance = wallet.getAvailableBalance();

        // Chặn tràn số khi cộng số dư.
        long newBalance = Math.addExact(currentBalance, amount);

        wallet.setAvailableBalance(newBalance);
        transaction.setBalanceAfter(newBalance);
        transaction.setStatus(TransactionStatus.SUCCESS);

        walletRepository.save(wallet);
        transactionRepository.save(transaction);

        actionLogService.log(ActionCode.DEPOSIT_CONFIRMED, wallet.getUser(), "PaymentTransaction", transaction.getId());
        return toResponse(transaction, null);
    }

    private DepositResponse toResponse(PaymentTransaction transaction, String paymentUrl) {

        return DepositResponse.builder()
                .transactionId(transaction.getId())
                .referenceCode(transaction.getReferenceCode())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .provider(transaction.getProvider())
                .paymentUrl(paymentUrl)
                .build();
    }
}
