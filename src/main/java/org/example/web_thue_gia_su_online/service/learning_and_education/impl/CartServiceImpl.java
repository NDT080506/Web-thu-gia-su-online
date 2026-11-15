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
import org.example.web_thue_gia_su_online.enums.learning_and_education.EnrollmentStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CartItemRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.CartService;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseEnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<CartItemResponse> getMyCart(Pageable pageable) {
        String userId = getCurrentUserId();
        return cartItemRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
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

        List<CartItem> cartItems = cartItemRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId, Pageable.unpaged())
                .getContent();

        List<CourseEnrollmentResponse> enrolled = new ArrayList<>();

        for (CartItem item : cartItems) {
            String courseId = item.getCourse().getId();

            // 1. Kiểm tra enrollment đã tồn tại chưa
            var existingEnrollment = courseEnrollmentRepository
                    .findByStudentIdAndCourseId(userId, courseId);

            if (existingEnrollment.isPresent()) {
                var existing = existingEnrollment.get();

                // 2. Enrollment PENDING: trả lại để tiếp tục thanh toán
                if (existing.getStatus() == EnrollmentStatus.PENDING) {
                    CourseEnrollmentResponse response =
                            courseEnrollmentRepository
                                    .findResponseById(existing.getId())
                                    .orElseThrow(() -> new AppException(
                                            ErrorCode.COURSE_ENROLLMENT_NOT_FOUND
                                    ));

                    enrolled.add(response);
                }

                // 3. Đã thanh toán hoặc hoàn thành: không cần thanh toán lại
                else if (existing.getStatus() == EnrollmentStatus.ACTIVE
                        || existing.getStatus() == EnrollmentStatus.COMPLETED) {

                    cartItemRepository.deleteByUserIdAndCourseId(
                            userId,
                            courseId
                    );
                }

                // 4. Enrollment CANCELLED: không cho thanh toán lại
                else if (existing.getStatus() == EnrollmentStatus.CANCELLED) {
                    throw new AppException(
                            ErrorCode.COURSE_ENROLLMENT_NOT_ACTIVE
                    );
                }

                continue;
            }

            // 5. Chưa tồn tại enrollment: tạo enrollment mới
            CourseEnrollmentCreationRequest request =
                    CourseEnrollmentCreationRequest.builder()
                            .courseId(courseId)
                            .build();

            CourseEnrollmentResponse newEnrollment =
                    courseEnrollmentService.enrollCourse(request);

            // 6. Enrollment cần thanh toán: đưa vào danh sách trả về
            if (newEnrollment.getStatus() == EnrollmentStatus.PENDING) {
                enrolled.add(newEnrollment);
            }

            // 7. Khóa học miễn phí đã ACTIVE: xóa khỏi giỏ hàng
            else if (newEnrollment.getStatus() == EnrollmentStatus.ACTIVE) {
                cartItemRepository.deleteByUserIdAndCourseId(
                        userId,
                        courseId
                );
            }
        }

        // KHÔNG xóa toàn bộ giỏ hàng tại đây.
        // Chỉ xóa từng khóa học sau khi thanh toán thành công.

        return enrolled;
    }
}
