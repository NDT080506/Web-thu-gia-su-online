package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentMethodCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.PaymentMethodService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentMethodControllerTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    @Test
    void createPaymentMethod_shouldReturnResponse() {
        PaymentMethodCreationRequest request = PaymentMethodCreationRequest.builder()
                .userId("user-1")
                .bankName("Bank")
                .accountNumber("123")
                .accountHolderName("Holder")
                .build();
        PaymentMethodResponse response = PaymentMethodResponse.builder().id("1").build();
        when(paymentMethodService.createPaymentMethod(eq(request))).thenReturn(response);

        ApiResponse<PaymentMethodResponse> apiResponse = paymentMethodController.createPaymentMethod(request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(paymentMethodService).createPaymentMethod(eq(request));
    }

    @Test
    void getPaymentMethod_shouldReturnResponse() {
        PaymentMethodResponse response = PaymentMethodResponse.builder().id("1").build();
        when(paymentMethodService.getPaymentMethod("1")).thenReturn(response);

        ApiResponse<PaymentMethodResponse> apiResponse = paymentMethodController.getPaymentMethod("1");

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(paymentMethodService).getPaymentMethod("1");
    }

    @Test
    void getPaymentMethodsByUserId_shouldReturnList() {
        List<PaymentMethodResponse> list =
                List.of(PaymentMethodResponse.builder().id("1").build());
        when(paymentMethodService.getPaymentMethodsByUserId("user-1")).thenReturn(list);

        ApiResponse<List<PaymentMethodResponse>> apiResponse =
                paymentMethodController.getPaymentMethodsByUserId("user-1");

        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
        verify(paymentMethodService).getPaymentMethodsByUserId("user-1");
    }

    @Test
    void setDefaultPaymentMethod_shouldReturnResponse() {
        PaymentMethodResponse response = PaymentMethodResponse.builder().id("1").build();
        when(paymentMethodService.setDefaultPaymentMethod("1")).thenReturn(response);

        ApiResponse<PaymentMethodResponse> apiResponse = paymentMethodController.setDefaultPaymentMethod("1");

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(paymentMethodService).setDefaultPaymentMethod("1");
    }

    @Test
    void deletePaymentMethod_shouldReturnSuccessMessage() {
        doNothing().when(paymentMethodService).deletePaymentMethod("1");

        ApiResponse<Void> apiResponse = paymentMethodController.deletePaymentMethod("1");

        assertNotNull(apiResponse);
        assertEquals("Payment method deleted successfully", apiResponse.getMessage());
        verify(paymentMethodService).deletePaymentMethod("1");
    }
}
