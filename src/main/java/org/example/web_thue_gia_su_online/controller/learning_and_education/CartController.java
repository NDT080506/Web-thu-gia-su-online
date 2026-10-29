package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CartItemRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CartItemResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.CartService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @GetMapping
    public ApiResponse<List<CartItemResponse>> getMyCart() {
        return ApiResponse.<List<CartItemResponse>>builder()
                .result(cartService.getMyCart())
                .build();
    }

    @PostMapping
    public ApiResponse<CartItemResponse> addToCart(@RequestBody @Valid CartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .result(cartService.addToCart(request))
                .build();
    }

    @DeleteMapping("/{courseId}")
    public ApiResponse<Void> removeFromCart(@PathVariable String courseId) {
        cartService.removeFromCart(courseId);
        return ApiResponse.<Void>builder()
                .message("Item removed from cart successfully")
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> clearCart() {
        cartService.clearCart();
        return ApiResponse.<Void>builder().message("Cart cleared successfully").build();
    }

    @PostMapping("/checkout")
    public ApiResponse<List<CourseEnrollmentResponse>> checkout() {
        return ApiResponse.<List<CourseEnrollmentResponse>>builder()
                .result(cartService.checkout())
                .build();
    }
}
