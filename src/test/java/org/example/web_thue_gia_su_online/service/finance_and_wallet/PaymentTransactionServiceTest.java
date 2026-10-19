package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.PaymentTransactionMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.impl.PaymentTransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionServiceTest {

    @Mock
    private PaymentTransactionRepository repository;

    @Mock
    private PaymentTransactionMapper mapper;

    @InjectMocks
    private PaymentTransactionServiceImpl service;

    @Test
    void createTransaction_shouldReturnResponse() {
        PaymentTransactionCreationRequest request = PaymentTransactionCreationRequest.builder()
                .sourceWalletId("wallet-1")
                .destinationWalletId("wallet-2")
                .amount(100000L)
                .type(TransactionType.DEPOSIT)
                .bookingId("booking-1")
                .balanceAfter(500000L)
                .build();

        PaymentTransaction txn = PaymentTransaction.builder().id("1").build();
        PaymentTransactionResponse response =
                PaymentTransactionResponse.builder().id("1").build();
        when(mapper.toPaymentTransaction(request)).thenReturn(txn);
        when(repository.save(any())).thenReturn(txn);
        when(mapper.toPaymentTransactionResponse(any())).thenReturn(response);

        PaymentTransactionResponse result = service.createTransaction(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getTransaction_shouldReturnResponse() {
        PaymentTransaction txn = PaymentTransaction.builder().id("1").build();
        PaymentTransactionResponse response =
                PaymentTransactionResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(txn));
        when(mapper.toPaymentTransactionResponse(txn)).thenReturn(response);

        PaymentTransactionResponse result = service.getTransaction("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getTransaction_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getTransaction("1"));
    }

    @Test
    void getTransactionsByWallet_shouldReturnList() {
        PaymentTransaction txn = PaymentTransaction.builder().id("1").build();
        PaymentTransactionResponse response =
                PaymentTransactionResponse.builder().id("1").build();
        when(repository.findBySourceWalletId("wallet-1")).thenReturn(List.of(txn));
        when(mapper.toPaymentTransactionResponse(txn)).thenReturn(response);

        List<PaymentTransactionResponse> result = service.getTransactionsByWallet("wallet-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getTransactions_shouldReturnList() {
        PaymentTransaction txn = PaymentTransaction.builder().id("1").build();
        PaymentTransactionResponse response =
                PaymentTransactionResponse.builder().id("1").build();
        when(repository.findAll()).thenReturn(List.of(txn));
        when(mapper.toPaymentTransactionResponse(txn)).thenReturn(response);

        List<PaymentTransactionResponse> result = service.getTransactions();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
