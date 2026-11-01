package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.WishlistItemResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.entity.learning_and_education.WishlistItem;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.WishlistItemRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.WishlistService;
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
public class WishlistServiceImpl implements WishlistService {

    WishlistItemRepository wishlistRepository;
    CourseRepository courseRepository;
    UserRepository userRepository;

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return auth.getName();
    }

    private WishlistItemResponse toResponse(WishlistItem item) {
        Course course = item.getCourse();
        String tutorName = course.getTutor() != null ? course.getTutor().getDisplayName() : null;
        String tutorId = course.getTutor() != null ? course.getTutor().getId() : null;

        return WishlistItemResponse.builder()
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
    public List<WishlistItemResponse> getMyWishlist() {
        String userId = getCurrentUserId();
        return wishlistRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public WishlistItemResponse addToWishlist(String courseId) {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Course course =
                courseRepository.findById(courseId).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        var existing = wishlistRepository.findByUserIdAndCourseId(userId, courseId);
        if (existing.isPresent()) {
            return toResponse(existing.get());
        }

        WishlistItem item = WishlistItem.builder().user(user).course(course).build();

        return toResponse(wishlistRepository.save(item));
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void removeFromWishlist(String courseId) {
        String userId = getCurrentUserId();
        wishlistRepository.deleteByUserIdAndCourseId(userId, courseId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public boolean isWishlisted(String courseId) {
        String userId = getCurrentUserId();
        return wishlistRepository.existsByUserIdAndCourseId(userId, courseId);
    }
}
