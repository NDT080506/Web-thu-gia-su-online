package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderMessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderMessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderMessageResponse;

public interface OrderMessageService {

    public OrderMessageResponse createOrderMessage(OrderMessageCreationRequest request, String orderId, String sendId);

    public OrderMessageResponse getOrderMessage(String id);

    public List<OrderMessageResponse> getOrderMessages();

    public OrderMessageResponse updateOrderMessage(OrderMessageUpdateRequest request, String id);

    public void deleteOrderMessage(String id);
}
