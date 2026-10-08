package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderMessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderMessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderMessageResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.entity.learning_and_education.OrderMessage;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.OrderMessageMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.OrderMessageRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.OrderRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.OrderMessageService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderMessageServiceImpl implements OrderMessageService {

    OrderMessageRepository orderMessageRepository;
    OrderRepository orderRepository;
    UserRepository userRepository;
    OrderMessageMapper orderMessageMapper;

    @Override
    public OrderMessageResponse createOrderMessage(
            OrderMessageCreationRequest request, String orderId, String senderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found"));

        OrderMessage message = orderMessageMapper.toOrderMessage(request);
        message.setOrder(order);
        message.setSender(sender);
        message.setCreatedAt(Instant.now());
        message.setUpdateAt(Instant.now());

        try {
            orderMessageRepository.save(message);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }

        return orderMessageMapper.toOrderMessageResponse(message);
    }

    @Override
    public OrderMessageResponse getOrderMessage(String id) {

        OrderMessage orderMessage =
                orderMessageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));

        return orderMessageMapper.toOrderMessageResponse(orderMessage);
    }

    @Override
    public List<OrderMessageResponse> getOrderMessages() {
        return orderMessageRepository.findAll().stream()
                .map(orderMessageMapper::toOrderMessageResponse)
                .toList();
    }

    @Override
    public OrderMessageResponse updateOrderMessage(OrderMessageUpdateRequest request, String id) {
        OrderMessage orderMessage =
                orderMessageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));

        orderMessageMapper.updateOrderMessage(orderMessage, request);
        orderMessage.setUpdateAt(Instant.now());

        return orderMessageMapper.toOrderMessageResponse(orderMessageRepository.save(orderMessage));
    }

    @Override
    public void deleteOrderMessage(String id) {
        OrderMessage orderMessage =
                orderMessageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));

        orderMessageRepository.delete(orderMessage);
    }
}
