package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @Test
    void getWallet_shouldReturnResponse() {
        WalletResponse response = WalletResponse.builder().id("1").build();
        when(walletService.getWallet("1")).thenReturn(response);
        ApiResponse<WalletResponse> apiResponse = walletController.getWallet("1");
        assertNotNull(apiResponse);
        verify(walletService).getWallet("1");
    }

    @Test
    void getWalletByUserId_shouldReturnResponse() {
        WalletResponse response = WalletResponse.builder().id("1").build();
        when(walletService.getWalletByUserId("user-1")).thenReturn(response);
        ApiResponse<WalletResponse> apiResponse = walletController.getWalletByUserId("user-1");
        assertNotNull(apiResponse);
        verify(walletService).getWalletByUserId("user-1");
    }

    @Test
    void getWallets_shouldReturnList() {
        List<WalletResponse> list = List.of(WalletResponse.builder().id("1").build());
        when(walletService.getWallets()).thenReturn(list);
        ApiResponse<List<WalletResponse>> apiResponse = walletController.getWallets();
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void updateWallet_shouldReturnResponse() {
        WalletUpdateRequest request =
                WalletUpdateRequest.builder().availableBalance(100000L).build();
        WalletResponse response = WalletResponse.builder().id("1").build();
        when(walletService.updateWallet(eq(request), eq("1"))).thenReturn(response);
        ApiResponse<WalletResponse> apiResponse = walletController.updateWallet("1", request);
        assertNotNull(apiResponse);
        verify(walletService).updateWallet(eq(request), eq("1"));
    }

    @Test
    void deleteWallet_shouldReturnSuccessMessage() {
        doNothing().when(walletService).deleteWallet("1");
        ApiResponse<Void> apiResponse = walletController.deleteWallet("1");
        assertNotNull(apiResponse);
        assertEquals("Wallet has been deleted", apiResponse.getMessage());
    }
}
