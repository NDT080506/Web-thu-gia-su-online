package org.example.web_thue_gia_su_online.mapper.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.PaymentMethodCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    @Mapping(target = "user.id", source = "userId")
    PaymentMethod toEntity(PaymentMethodCreationRequest request);

    @Mapping(target = "userId", source = "user.id")
    PaymentMethodResponse toResponse(PaymentMethod entity);
}
