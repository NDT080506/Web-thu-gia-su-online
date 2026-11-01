package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WithdrawalRequest;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PayoutResult;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Withdrawal;

public interface WithdrawalPersistenceService {
    public Withdrawal reserve(String userId, WithdrawalRequest request, String referenceCode);

    public void complete(String withdrawalId, PayoutResult result);
}
