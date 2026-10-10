package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderMessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderMessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderMessageResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.impl.OrderMessageServiceImpl;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/ordermessages")
public class OrderMessageController {

    OrderMessageServiceImpl orderMessageService;

    @PostMapping("/{orderId}/{sendId}")
    ApiResponse<OrderMessageResponse> createOrderMessage(
            @RequestBody @Valid OrderMessageCreationRequest request,
            @PathVariable String orderId,
            @PathVariable String sendId) {
        return ApiResponse.<OrderMessageResponse>builder()
                .result(orderMessageService.createOrderMessage(request, orderId, sendId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<OrderMessageResponse> getOrderMessage(@PathVariable String id) {
        return ApiResponse.<OrderMessageResponse>builder()
                .result(orderMessageService.getOrderMessage(id))
                .build();
    }

    @GetMapping
    ApiResponse<List<OrderMessageResponse>> getOrderMessages() {
        return ApiResponse.<List<OrderMessageResponse>>builder()
                .result(orderMessageService.getOrderMessages())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<OrderMessageResponse> updateOrderMessage(
            @PathVariable String id, @RequestBody @Valid OrderMessageUpdateRequest request) {
        return ApiResponse.<OrderMessageResponse>builder()
                .result(orderMessageService.updateOrderMessage(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteOrderMessage(@PathVariable String id) {
        orderMessageService.deleteOrderMessage(id);

        return ApiResponse.<Void>builder()
                .message("Order message has been deleted")
                .build();
    }
}
