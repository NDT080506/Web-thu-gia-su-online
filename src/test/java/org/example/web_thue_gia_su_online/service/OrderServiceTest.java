package org.example.web_thue_gia_su_online.service;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.entity.learning_and_education.ServicePackage;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.OrderMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.OrderRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ServicePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServicePackageRepository servicePackageRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_shouldReturnOrderResponse_whenValid() {
        // GIVEN
        String studentId = "student-1";
        String tutorId = "tutor-1";
        String packageId = "package-1";

        OrderCreationRequest request = new OrderCreationRequest();

        User student = new User();
        User tutor = new User();
        ServicePackage servicePackage = new ServicePackage();

        Order order = new Order();
        OrderResponse expectedResponse = new OrderResponse();

        Mockito.when(userRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        Mockito.when(userRepository.findById(tutorId))
                .thenReturn(Optional.of(tutor));

        Mockito.when(servicePackageRepository.findById(packageId))
                .thenReturn(Optional.of(servicePackage));

        Mockito.when(orderMapper.toOrder(request))
                .thenReturn(order);

        Mockito.when(orderMapper.toOrderResponse(order))
                .thenReturn(expectedResponse);

        // WHEN
        OrderResponse actualResponse = orderService.createOrder(
                request, studentId, tutorId, packageId
        );

        // THEN
        assertSame(expectedResponse, actualResponse);

        assertSame(student, order.getStudent());
        assertSame(tutor, order.getTutor());
        assertSame(servicePackage, order.getServicePackage());

        assertNotNull(order.getCreated_at());
        assertNotNull(order.getUpdateAt());

        Mockito.verify(orderRepository).save(order);
        Mockito.verify(orderMapper).toOrderResponse(order);
    }
}
