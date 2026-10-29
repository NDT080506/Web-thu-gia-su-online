package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.util.ArrayList;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CartItemRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CartItemResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CartItem;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CartItemRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.CartService;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseEnrollmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    CartItemRepository cartItemRepository;
    CourseRepository courseRepository;
    UserRepository userRepository;
    CourseEnrollmentRepository courseEnrollmentRepository;
    CourseEnrollmentService courseEnrollmentService;

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return auth.getName();
    }

    private CartItemResponse toResponse(CartItem item) {
        Course course = item.getCourse();
        String tutorName = course.getTutor() != null ? course.getTutor().getDisplayName() : null;
        String tutorId = course.getTutor() != null ? course.getTutor().getId() : null;

        return CartItemResponse.builder()
                .id(item.getId())
                .courseId(course.getId())
                .courseTitle(course.getTitle())
                .price(course.getPrice())
                .tutorId(tutorId)
                .tutorName(tutorName)
                .createdAt(item.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<CartItemResponse> getMyCart() {
        String userId = getCurrentUserId();
        return cartItemRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public CartItemResponse addToCart(CartItemRequest request) {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        if (!course.isActive()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }

        // Nếu đã có trong giỏ hàng, trả về luôn mục hiện có
        var existing = cartItemRepository.findByUserIdAndCourseId(userId, course.getId());
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        CartItem item = CartItem.builder().user(user).course(course).build();

        return toResponse(cartItemRepository.save(item));
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void removeFromCart(String courseId) {
        String userId = getCurrentUserId();
        cartItemRepository.deleteByUserIdAndCourseId(userId, courseId);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void clearCart() {
        String userId = getCurrentUserId();
        cartItemRepository.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public List<CourseEnrollmentResponse> checkout() {
        String userId = getCurrentUserId();
        List<CartItem> cartItems = cartItemRepository.findAllByUserIdOrderByCreatedAtDesc(userId);

        List<CourseEnrollmentResponse> enrolled = new ArrayList<>();
        for (CartItem item : cartItems) {
            String courseId = item.getCourse().getId();
            // Nếu chưa ghi danh thì gọi enrollCourse
            boolean alreadyEnrolled = courseEnrollmentRepository
                    .findByStudentIdAndCourseId(userId, courseId)
                    .isPresent();
            if (!alreadyEnrolled) {
                CourseEnrollmentCreationRequest req = CourseEnrollmentCreationRequest.builder()
                        .courseId(courseId)
                        .build();
                enrolled.add(courseEnrollmentService.enrollCourse(req));
            }
        }

        // Xóa sạch giỏ hàng sau khi checkout thành công
        cartItemRepository.deleteAllByUserId(userId);

        return enrolled;
    }
}
