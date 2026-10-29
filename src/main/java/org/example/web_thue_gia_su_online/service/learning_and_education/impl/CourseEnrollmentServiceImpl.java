package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.EnrollmentStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.CourseEnrollmentMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.EscrowReleaseService;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseEnrollmentService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    CourseEnrollmentRepository courseEnrollmentRepository;
    UserRepository userRepository;
    CourseRepository courseRepository;
    CourseEnrollmentMapper courseEnrollmentMapper;
    EscrowReleaseService escrowReleaseService;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public CourseEnrollmentResponse enrollCourse(CourseEnrollmentCreationRequest request) {
        // 1. Lấy studentId từ JWT
        String studentId = getCurrentUserId();
        User student = userRepository.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. Kiểm tra course tồn tại và đang active
        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));

        if (!course.isActive()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 3. Không cho phép đăng ký trùng
        courseEnrollmentRepository
                .findByStudentIdAndCourseId(student.getId(), course.getId())
                .ifPresent(existing -> {
                    throw new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND);
                });

        // 4. Tính giá từ course price, không tin client
        long totalPrice = course.getPrice();
        long platformFee = totalPrice * course.getPlatformFeePercent() / 100;
        long tutorIncome = totalPrice - platformFee;

        CourseEnrollment enrollment = CourseEnrollment.builder()
                .student(student)
                .course(course)
                .totalPrice(totalPrice)
                .platformFee(platformFee)
                .tutorIncome(tutorIncome)
                .status(EnrollmentStatus.PENDING)
                .build();

        return courseEnrollmentMapper.toResponse(courseEnrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public CourseEnrollmentResponse getEnrollment(String id) {
        CourseEnrollment enrollment = courseEnrollmentRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));
        return courseEnrollmentMapper.toResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<CourseEnrollmentResponse> getEnrollmentsByStudent() {
        String studentId = getCurrentUserId();
        return courseEnrollmentRepository.findByStudentId(studentId).stream()
                .map(courseEnrollmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'TUTOR')")
    public List<CourseEnrollmentResponse> getEnrollmentsByCourse(String courseId) {
        // Tutor chỉ được xem enrollment của course mình sở hữu
        String currentUserId = getCurrentUserId();
        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));

        // Nếu là tutor, kiểm tra ownership
        boolean isOwner = course.getTutor().getId().equals(currentUserId);
        if (!isOwner) {
            throw new AccessDeniedException("Bạn không có quyền xem enrollment của khóa học này");
        }

        return courseEnrollmentRepository.findByCourseId(courseId).stream()
                .map(courseEnrollmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public CourseEnrollmentResponse updateEnrollment(String id) {
        CourseEnrollment enrollment = courseEnrollmentRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));

        // Resource-level authorization
        String currentUserId = getCurrentUserId();
        if (!enrollment.getStudent().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không có quyền hủy enrollment này");
        }

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        return courseEnrollmentMapper.toResponse(courseEnrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('TUTOR')")
    public CourseEnrollmentResponse completeEnrollment(String enrollmentId) {
        // 1. Lấy enrollment
        CourseEnrollment enrollment = courseEnrollmentRepository
                .findByIdForUpdate(enrollmentId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));

        // 2. Lấy ID người đang đăng nhập
        String currentUserId = getCurrentUserId();

        // 3. Kiểm tra gia sư sở hữu khóa học
        String tutorId = enrollment.getCourse().getTutor().getId();

        if (!tutorId.equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không có quyền xác nhận hoàn thành khóa học này");
        }

        // 4. Chỉ cho phép hoàn thành enrollment đang ACTIVE
        if (enrollment.getStatus() == EnrollmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.COURSE_ENROLLMENT_ALREADY_COMPLETED);
        }

        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_ACTIVE);
        }

        // 5. Cập nhật trạng thái hoàn thành
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setCompletedAt(Instant.now());
        courseEnrollmentRepository.save(enrollment);

        // 6. Giải ngân tiền đã giữ cho enrollment
        escrowReleaseService.releaseEnrollmentEscrow(enrollment);

        return courseEnrollmentMapper.toResponse(enrollment);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEnrollment(String id) {
        CourseEnrollment enrollment = courseEnrollmentRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_FOUND));
        courseEnrollmentRepository.delete(enrollment);
    }
}
