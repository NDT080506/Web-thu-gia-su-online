package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderMessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderMessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderMessageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.OrderMessage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMessageMapper {

    OrderMessage toOrderMessage(OrderMessageCreationRequest request);

    OrderMessageResponse toOrderMessageResponse(OrderMessage orderMessage);

    void updateOrderMessage(@MappingTarget OrderMessage orderMessage, OrderMessageUpdateRequest request);
}
