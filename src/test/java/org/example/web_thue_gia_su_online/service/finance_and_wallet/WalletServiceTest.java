package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.WalletMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.impl.WalletServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletMapper mapper;

    @InjectMocks
    private WalletServiceImpl service;

    @Test
    void getWallet_shouldReturnResponse() {
        Wallet wallet = Wallet.builder().id("1").build();
        WalletResponse response = WalletResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(wallet));
        when(mapper.toWalletResponse(any())).thenReturn(response);

        WalletResponse result = service.getWallet("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getWallet_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getWallet("1"));
    }

    @Test
    void getWalletByUserId_shouldReturnResponse() {
        Wallet wallet = Wallet.builder().id("1").build();
        WalletResponse response = WalletResponse.builder().id("1").build();
        when(repository.findByUserId("user-1")).thenReturn(Optional.of(wallet));
        when(mapper.toWalletResponse(any())).thenReturn(response);

        WalletResponse result = service.getWalletByUserId("user-1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getWallets_shouldReturnList() {
        Wallet wallet = Wallet.builder().id("1").build();
        WalletResponse response = WalletResponse.builder().id("1").build();
        when(repository.findAll()).thenReturn(List.of(wallet));
        when(mapper.toWalletResponse(any())).thenReturn(response);

        List<WalletResponse> result = service.getWallets();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateWallet_shouldReturnResponse() {
        Wallet wallet = Wallet.builder().id("1").build();
        WalletResponse response = WalletResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(wallet));
        when(repository.save(any())).thenReturn(wallet);
        when(mapper.toWalletResponse(any())).thenReturn(response);

        WalletResponse result = service.updateWallet(any(), "1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void deleteWallet_shouldSucceed() {
        Wallet wallet = Wallet.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(wallet));
        doNothing().when(repository).delete(wallet);

        service.deleteWallet("1");

        verify(repository).delete(wallet);
    }
}
