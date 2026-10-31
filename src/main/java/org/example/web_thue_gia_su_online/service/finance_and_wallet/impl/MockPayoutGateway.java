package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PayoutResult;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PayoutGateway;
import org.springframework.stereotype.Component;

@Component
public class MockPayoutGateway implements PayoutGateway {

    @Override
    public PayoutResult transfer(
            String referenceCode, long amount, String bankName, String accountNumber, String accountHolder) {
        // Giả lập chuyển khoản thành công.
        return new PayoutResult(true, "MOCK-PAYOUT-" + referenceCode, null);
    }
}
