package org.example.web_thue_gia_su_online.service.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.WalletType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletService {

    WalletResponse createWallet(WalletType type);

    WalletResponse getWallet(String id);

    Page<WalletResponse> getWalletByUserId(Pageable pageable);

    Page<WalletResponse> getWallets(Pageable pageable);

    WalletResponse updateWallet(WalletUpdateRequest request, String id);

    void deleteWallet(String id);
}
