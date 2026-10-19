package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentTransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionControllerTest {

    @Mock
    private PaymentTransactionService paymentTransactionService;

    @InjectMocks
    private PaymentTransactionController paymentTransactionController;

    @Test
    void createTransaction_shouldReturnResponse() {
        PaymentTransactionCreationRequest request = PaymentTransactionCreationRequest.builder()
                .type(TransactionType.PAYMENT)
                .amount(100000L)
                .balanceAfter(500000L)
                .build();
        PaymentTransactionResponse response =
                PaymentTransactionResponse.builder().id("1").build();
        when(paymentTransactionService.createTransaction(eq(request))).thenReturn(response);
        ApiResponse<PaymentTransactionResponse> apiResponse = paymentTransactionController.createTransaction(request);
        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(paymentTransactionService).createTransaction(eq(request));
    }

    @Test
    void getTransaction_shouldReturnResponse() {
        PaymentTransactionResponse response =
                PaymentTransactionResponse.builder().id("1").build();
        when(paymentTransactionService.getTransaction("1")).thenReturn(response);
        ApiResponse<PaymentTransactionResponse> apiResponse = paymentTransactionController.getTransaction("1");
        assertNotNull(apiResponse);
        verify(paymentTransactionService).getTransaction("1");
    }

    @Test
    void getTransactionsByWallet_shouldReturnList() {
        List<PaymentTransactionResponse> list =
                List.of(PaymentTransactionResponse.builder().id("1").build());
        when(paymentTransactionService.getTransactionsByWallet("wallet-1")).thenReturn(list);
        ApiResponse<List<PaymentTransactionResponse>> apiResponse =
                paymentTransactionController.getTransactionsByWallet("wallet-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getTransactionsByBooking_shouldReturnList() {
        List<PaymentTransactionResponse> list =
                List.of(PaymentTransactionResponse.builder().id("1").build());
        when(paymentTransactionService.getTransactionsByBooking("booking-1")).thenReturn(list);
        ApiResponse<List<PaymentTransactionResponse>> apiResponse =
                paymentTransactionController.getTransactionsByBooking("booking-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getTransactions_shouldReturnList() {
        List<PaymentTransactionResponse> list =
                List.of(PaymentTransactionResponse.builder().id("1").build());
        when(paymentTransactionService.getTransactions()).thenReturn(list);
        ApiResponse<List<PaymentTransactionResponse>> apiResponse = paymentTransactionController.getTransactions();
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }
}
