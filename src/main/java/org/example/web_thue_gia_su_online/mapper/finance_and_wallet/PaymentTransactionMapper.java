package org.example.web_thue_gia_su_online.mapper.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentTransactionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentTransactionResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentTransactionMapper {

    PaymentTransaction toPaymentTransaction(PaymentTransactionCreationRequest request);

    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "order.id", target = "orderId")
    PaymentTransactionResponse toPaymentTransactionResponse(PaymentTransaction transaction);
}