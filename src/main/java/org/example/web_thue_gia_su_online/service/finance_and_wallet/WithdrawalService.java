package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WithdrawalRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WithdrawalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WithdrawalService {
    WithdrawalResponse createWithdrawal(WithdrawalRequest request);

    Page<WithdrawalResponse> getMyWithdrawals(Pageable pageable);

    WithdrawalResponse getMyWithdrawal(String withdrawalId);

    public Page<WithdrawalResponse> getAllWithdrawals(Pageable pageable);
}
