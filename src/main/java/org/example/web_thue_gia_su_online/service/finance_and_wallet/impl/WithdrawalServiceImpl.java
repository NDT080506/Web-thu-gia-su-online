package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WithdrawalRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WithdrawalResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PayoutResult;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Withdrawal;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WithdrawalRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PayoutGateway;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WithdrawalPersistenceService;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WithdrawalService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawalServiceImpl implements WithdrawalService {

    WithdrawalPersistenceService persistenceService;
    WithdrawalRepository withdrawalRepository;
    PayoutGateway payoutGateway;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    public WithdrawalResponse createWithdrawal(WithdrawalRequest request) {
        String userId = getCurrentUserId();

        String referenceCode = UUID.randomUUID().toString();

        // Giai đoạn 1: giữ tiền và tạo yêu cầu trong DB
        Withdrawal withdrawal = persistenceService.reserve(userId, request, referenceCode);

        // Giai đoạn 2: gọi nhà cung cấp payout ngoài transaction DB
        PayoutResult result;

        try {
            result = payoutGateway.transfer(
                    referenceCode,
                    withdrawal.getAmount(),
                    withdrawal.getBankName(),
                    withdrawal.getAccountNumber(),
                    withdrawal.getAccountHolder());
        } catch (Exception exception) {
            result = new PayoutResult(false, null, "Payout provider error");
        }

        // Giai đoạn 3: cập nhật kết quả và hoàn tiền nếu thất bại
        persistenceService.complete(withdrawal.getId(), result);

        return findWithdrawalResponse(userId, withdrawal.getId());
    }

    private WithdrawalResponse findWithdrawalResponse(String userId, String withdrawalId) {
        Withdrawal withdrawal = withdrawalRepository
                .findByIdAndUser_Id(withdrawalId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.WITHDRAWAL_NOT_FOUND));

        return toResponse(withdrawal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WithdrawalResponse> getMyWithdrawals() {
        String userId = getCurrentUserId();

        return withdrawalRepository.findByUser_Id(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WithdrawalResponse getMyWithdrawal(String withdrawalId) {
        String userId = getCurrentUserId();

        Withdrawal withdrawal = withdrawalRepository
                .findByIdAndUser_Id(withdrawalId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.WITHDRAWAL_NOT_FOUND));

        return toResponse(withdrawal);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<WithdrawalResponse> getAllWithdrawals() {
        return withdrawalRepository.findAll().stream().map(this::toResponse).toList();
    }

    private WithdrawalResponse toResponse(Withdrawal withdrawal) {
        return WithdrawalResponse.builder()
                .id(withdrawal.getId())
                .amount(withdrawal.getAmount())
                .bankName(withdrawal.getBankName())
                .accountNumber(withdrawal.getAccountNumber())
                .accountHolder(withdrawal.getAccountHolder())
                .status(withdrawal.getStatus())
                .providerReference(withdrawal.getProviderReference())
                .failureReason(withdrawal.getFailureReason())
                .createdAt(withdrawal.getCreatedAt())
                .processedAt(withdrawal.getProcessedAt())
                .build();
    }
}
