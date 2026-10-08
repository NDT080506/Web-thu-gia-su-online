package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderResponse;

public interface OrderService {
    public OrderResponse createOrder(OrderCreationRequest request, String studentId, String tutorId, String packageId);

    public OrderResponse getOrder(String id);

    public List<OrderResponse> getOrders();

    public OrderResponse updateOrder(OrderUpdateRequest request, String id);

    public void deleteOrder(String id);
}
