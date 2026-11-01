package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WithdrawalRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WithdrawalResponse;

public interface WithdrawalService {
    WithdrawalResponse createWithdrawal(WithdrawalRequest request);

    List<WithdrawalResponse> getMyWithdrawals();

    WithdrawalResponse getMyWithdrawal(String withdrawalId);

    public List<WithdrawalResponse> getAllWithdrawals();
}
