package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PayoutResult;


public interface PayoutGateway {

    PayoutResult transfer(
            String referenceCode, long amount, String bankName, String accountNumber, String accountHolder);
}
