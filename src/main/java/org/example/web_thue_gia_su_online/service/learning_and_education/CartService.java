package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CartItemRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CartItemResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;

public interface CartService {
    List<CartItemResponse> getMyCart();

    CartItemResponse addToCart(CartItemRequest request);

    void removeFromCart(String courseId);

    void clearCart();

    List<CourseEnrollmentResponse> checkout();
}
