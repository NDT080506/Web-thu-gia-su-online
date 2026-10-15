package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;

public interface PaymentTransactionService {

    PaymentTransactionResponse createTransaction(PaymentTransactionCreationRequest request);

    PaymentTransactionResponse getTransaction(String id);

    List<PaymentTransactionResponse> getTransactionsByWallet(String walletId);

    List<PaymentTransactionResponse> getTransactionsByBooking(String bookingId);

    List<PaymentTransactionResponse> getTransactions();
}
