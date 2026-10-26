package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ReviewCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ReviewUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ReviewResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Review;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.example.web_thue_gia_su_online.enums.BookingStatus;
import org.example.web_thue_gia_su_online.enums.EnrollmentStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.ReviewMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ReviewRepository;
import org.example.web_thue_gia_su_online.repository.tutoring.TutorProfileRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.ReviewService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CourseEnrollmentRepository courseEnrollmentRepository;
    TutorProfileRepository tutorProfileRepository;
    ReviewMapper reviewMapper;

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public ReviewResponse createReview(ReviewCreationRequest request) {
        boolean hasBooking = request.getBookingId() != null;
        boolean hasEnrollment = request.getEnrollmentId() != null;

        if (hasBooking == hasEnrollment) {
            throw new IllegalArgumentException("Phải cung cấp đúng một trong bookingId hoặc enrollmentId");
        }

        String reviewerId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        User reviewer =
                userRepository.findById(reviewerId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User tutor;
        Booking booking = null;
        CourseEnrollment enrollment = null;

        if (hasBooking) {
            booking = bookingRepository
                    .findById(request.getBookingId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

            if (!booking.getStudent().getId().equals(reviewerId)) {
                throw new AccessDeniedException("Bạn không phải học viên của booking này");
            }

            if (booking.getStatus() != BookingStatus.COMPLETED) {
                throw new IllegalStateException("Chỉ booking đã hoàn thành mới được đánh giá");
            }

            if (booking.getTutor() == null) {
                throw new IllegalStateException("Booking chưa xác định gia sư");
            }

            if (reviewRepository.existsByBooking_Id(booking.getId())) {
                throw new IllegalStateException("Booking này đã được đánh giá");
            }

            tutor = booking.getTutor();

        } else {
            enrollment = courseEnrollmentRepository
                    .findById(request.getEnrollmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));

            if (!enrollment.getStudent().getId().equals(reviewerId)) {
                throw new AccessDeniedException("Bạn không phải học viên của lượt đăng ký này");
            }

            if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
                throw new IllegalStateException("Khóa học chưa hoàn thành");
            }

            if (reviewRepository.existsByEnrollment_Id(enrollment.getId())) {
                throw new IllegalStateException("Lượt đăng ký này đã được đánh giá");
            }

            tutor = enrollment.getCourse().getTutor();
        }

        Review review = Review.builder()
                .reviewer(reviewer)
                .tutor(tutor)
                .booking(booking)
                .enrollment(enrollment)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        updateTutorRating(tutor.getId());

        return reviewMapper.toResponse(savedReview);
    }

    @Override
    @PreAuthorize("hasRole('TUTOR')")
    public List<ReviewResponse> getTutorReviews() {

        String tutorId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        return reviewRepository.findAllByTutor_Id(tutorId).stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public ReviewResponse updateReview(ReviewUpdateRequest request) {
        String reviewerId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        Review review = reviewRepository
                .findByReviewer_Id(reviewerId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        reviewMapper.updateReview(request, review);
        reviewRepository.save(review);
        updateTutorRating(review.getTutor().getId());

        return reviewMapper.toResponse(review);
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public void deleteMyReview() {
        String reviewerId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        Review review = reviewRepository
                .findByReviewer_Id(reviewerId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }

    private void updateTutorRating(String tutorId) {
        Double average = reviewRepository.calculateAverageRating(tutorId);

        TutorProfile profile = tutorProfileRepository
                .findByUserId(tutorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        profile.setRating(average == null ? null : BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP));

        tutorProfileRepository.save(profile);
    }
}
