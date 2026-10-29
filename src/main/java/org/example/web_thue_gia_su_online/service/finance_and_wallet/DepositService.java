package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.DepositRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.DepositResponse;

public interface DepositService {

    DepositResponse createDeposit(String userId, DepositRequest request);

    DepositResponse confirmDeposit(String userId, String referenceCode);
}
