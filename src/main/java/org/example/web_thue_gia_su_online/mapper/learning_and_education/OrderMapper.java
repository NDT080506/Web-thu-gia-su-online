package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrder(OrderCreationRequest request);

    OrderResponse toOrderResponse(Order order);

    void updateOrder(@MappingTarget Order order, OrderUpdateRequest request);
}
