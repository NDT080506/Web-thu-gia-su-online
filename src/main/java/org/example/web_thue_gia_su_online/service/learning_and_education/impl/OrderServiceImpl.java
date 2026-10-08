package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.entity.learning_and_education.ServicePackage;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.OrderMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.OrderRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ServicePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.OrderService;
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
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderMapper orderMapper;
    UserRepository userRepository;
    ServicePackageRepository servicePackageRepository;

    @Override
    public OrderResponse createOrder(OrderCreationRequest request, String studentId, String tutorId, String packageId) {

        User student = userRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));

        User tutor = userRepository.findById(tutorId).orElseThrow(() -> new RuntimeException("Tutor not found"));

        ServicePackage servicePackage = servicePackageRepository
                .findById(packageId)
                .orElseThrow(() -> new RuntimeException("Service package not found"));

        Order order = orderMapper.toOrder(request);
        order.setCreated_at(Instant.now());
        order.setUpdateAt(Instant.now());

        order.setStudent(student);
        order.setTutor(tutor);
        order.setServicePackage(servicePackage);

        try {
            orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getOrder(String id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse updateOrder(OrderUpdateRequest request, String id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        orderMapper.updateOrder(order, request);
        order.setUpdateAt(Instant.now());

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(String id) {

        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        orderRepository.delete(order);
    }
}
