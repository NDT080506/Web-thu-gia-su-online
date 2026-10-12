package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.OrderService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/orders")
public class OrderController {

    OrderService orderService;

    @PostMapping("/{studentId}/{tutorId}/{packageId}")
    ApiResponse<OrderResponse> createOrder(
            @RequestBody @Valid OrderCreationRequest request,
            @PathVariable String studentId,
            @PathVariable String tutorId,
            @PathVariable String packageId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(request, studentId, tutorId, packageId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<OrderResponse> getOrder(@PathVariable String id) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrder(id))
                .build();
    }

    @GetMapping
    ApiResponse<List<OrderResponse>> getOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getOrders())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<OrderResponse> updateOrder(@PathVariable String id, @RequestBody @Valid OrderUpdateRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrder(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);

        return ApiResponse.<Void>builder().message("Order has been deleted").build();
    }
}
