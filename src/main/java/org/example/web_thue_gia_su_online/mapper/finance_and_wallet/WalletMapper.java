package org.example.web_thue_gia_su_online.mapper.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    Wallet toWallet(WalletCreationRequest request);

    @Mapping(source = "user.id", target = "userId")
    WalletResponse toWalletResponse(Wallet wallet);

    void updateWallet(@MappingTarget Wallet wallet, WalletUpdateRequest request);
}