package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;

public interface WalletService {

    WalletResponse createWallet(WalletCreationRequest request, String userId);

    WalletResponse getWallet(String id);

    WalletResponse getWalletByUserId(String userId);

    List<WalletResponse> getWallets();

    WalletResponse updateWallet(WalletUpdateRequest request, String id);

    void deleteWallet(String id);
}
