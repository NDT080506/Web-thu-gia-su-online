package org.example.web_thue_gia_su_online.mapper.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper {

    @Mapping(target = "sourceWallet.id", source = "sourceWalletId")
    @Mapping(target = "destinationWallet.id", source = "destinationWalletId")
    @Mapping(target = "booking.id", source = "bookingId")
    PaymentTransaction toPaymentTransaction(PaymentTransactionCreationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "sourceWalletId", source = "sourceWallet.id")
    @Mapping(target = "destinationWalletId", source = "destinationWallet.id")
    @Mapping(target = "bookingId", source = "booking.id")
    PaymentTransactionResponse toPaymentTransactionResponse(PaymentTransaction transaction);
}
