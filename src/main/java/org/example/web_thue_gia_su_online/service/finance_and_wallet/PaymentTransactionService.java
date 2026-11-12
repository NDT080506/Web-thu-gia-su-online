package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentTransactionService {

    PaymentTransactionResponse createTransaction(PaymentTransactionCreationRequest request);

    PaymentTransactionResponse getTransaction(String id);

    Page<PaymentTransactionResponse> getTransactionsByWallet(String walletId, Pageable pageable);

    Page<PaymentTransactionResponse> getTransactionsByBooking(String bookingId, Pageable pageable);

    Page<PaymentTransactionResponse> getTransactions(Pageable pageable);
}
