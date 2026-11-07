package org.example.web_thue_gia_su_online.service.disputing_and_reporting.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.ActionCode;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;
import org.example.web_thue_gia_su_online.enums.TransactionStatus;
import org.example.web_thue_gia_su_online.enums.TransactionType;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.disputing_and_reporting.DisputeMapper;
import org.example.web_thue_gia_su_online.repository.disputing_and_reporting.DisputeRepository;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.PaymentTransactionRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.service.disputing_and_reporting.DisputeService;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.RefundService;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DisputeServiceImpl implements DisputeService {

    DisputeRepository disputeRepository;
    BookingRepository bookingRepository;
    DisputeMapper disputeMapper;
    CourseEnrollmentRepository enrollmentRepository;
    UserRepository userRepository;
    PaymentTransactionRepository paymentTransactionRepository;
    RefundService refundService;
    ActionLogService actionLogService;

    private String getCurrentStudentId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public DisputeResponse createDispute(DisputeCreationRequest request) {
        boolean hasBooking = request.getBookingId() != null;
        boolean hasEnrollment = request.getEnrollmentId() != null;

        if (hasBooking == hasEnrollment) {
            throw new AppException(ErrorCode.INVALID_DISPUTE_TARGET);
        }

        String studentId = getCurrentStudentId();

        User creator = userRepository.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Dispute.DisputeBuilder builder = Dispute.builder()
                .createdBy(creator)
                .reason(request.getReason())
                .evidenceUrl(request.getEvidenceUrl())
                .status(DisputeStatus.UNDER_REVIEW);

        if (hasBooking) {
            Booking booking = bookingRepository
                    .findById(request.getBookingId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

            validateBookingParticipant(booking, studentId);
            validateBookingPayment(booking);

            boolean exists = disputeRepository.existsByBooking_IdAndStatus(booking.getId(), DisputeStatus.UNDER_REVIEW);

            if (exists) {
                throw new AppException(ErrorCode.DISPUTE_ALREADY_ACTIVE);
            }

            builder.booking(booking);
            builder.refundAmount(booking.getTotalPrice());
        }

        if (hasEnrollment) {
            CourseEnrollment enrollment = enrollmentRepository
                    .findById(request.getEnrollmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_ENROLLMENT_NOT_ACTIVE));

            validateEnrollmentParticipant(enrollment, studentId);
            validateEnrollmentPayment(enrollment);

            boolean exists =
                    disputeRepository.existsByEnrollment_IdAndStatus(enrollment.getId(), DisputeStatus.UNDER_REVIEW);

            if (exists) {
                throw new AppException(ErrorCode.DISPUTE_ALREADY_ACTIVE);
            }

            builder.enrollment(enrollment);
            builder.refundAmount(enrollment.getTotalPrice());
        }

        Dispute dispute = disputeRepository.save(builder.build());
        actionLogService.log(ActionCode.CREATE_DISPUTE, creator, "Dispute", dispute.getId());
        return disputeMapper.toResponse(dispute);
    }

    private void validateBookingParticipant(Booking booking, String studentId) {
        if (!booking.getStudent().getId().equals(studentId)) {
            throw new AppException(ErrorCode.INVALID_PARTICIPANT);
        }
    }

    private void validateEnrollmentParticipant(CourseEnrollment enrollment, String studentId) {
        if (!enrollment.getStudent().getId().equals(studentId)) {
            throw new AppException(ErrorCode.INVALID_PARTICIPANT);
        }
    }

    private void validateBookingPayment(Booking booking) {
        boolean hasHold = paymentTransactionRepository.existsByBooking_IdAndTypeAndStatus(
                booking.getId(), TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);
        boolean hasRelease = paymentTransactionRepository.existsByBooking_IdAndTypeAndStatus(
                booking.getId(), TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS);

        if (!hasHold && !hasRelease) {
            throw new AppException(ErrorCode.PAYMENT_NOT_COMPLETED);
        }
    }

    private void validateEnrollmentPayment(CourseEnrollment enrollment) {
        boolean hasHold = paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatus(
                enrollment.getId(), TransactionType.ESCROW_HOLD, TransactionStatus.SUCCESS);
        boolean hasRelease = paymentTransactionRepository.existsByEnrollment_IdAndTypeAndStatus(
                enrollment.getId(), TransactionType.ESCROW_RELEASE, TransactionStatus.SUCCESS);

        if (!hasHold && !hasRelease) {
            throw new AppException(ErrorCode.PAYMENT_NOT_COMPLETED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<DisputeResponse> getMyDisputes() {
        String studentId = getCurrentStudentId();
        return disputeRepository.findByCreatedBy_Id(studentId).stream()
                .map(disputeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public DisputeResponse getMyDispute(String disputeId) {
        String studentId = getCurrentStudentId();

        Dispute dispute = disputeRepository
                .findByIdAndCreatedBy_Id(disputeId, studentId)
                .orElseThrow(() -> new AppException(ErrorCode.DISPUTE_NOT_FOUND));

        return disputeMapper.toResponse(dispute);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public void cancelDispute(String disputeId) {
        String studentId = getCurrentStudentId();

        Dispute dispute = disputeRepository
                .findByIdAndCreatedBy_Id(disputeId, studentId)
                .orElseThrow(() -> new AppException(ErrorCode.DISPUTE_NOT_FOUND));

        if (dispute.getStatus() == DisputeStatus.REJECTED
                || dispute.getStatus() == DisputeStatus.APPROVED
                || dispute.getStatus() == DisputeStatus.CANCELLED) {
            throw new AppException(ErrorCode.DISPUTE_CANNOT_BE_CANCELLED);
        }

        dispute.setStatus(DisputeStatus.CANCELLED);
        disputeRepository.save(dispute);
        actionLogService.log(ActionCode.CANCEL_DISPUTE, dispute.getCreatedBy(), "Dispute", disputeId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<DisputeResponse> getAllDisputes(DisputeStatus status) {
        List<Dispute> disputes = status == null ? disputeRepository.findAll() : disputeRepository.findByStatus(status);

        return disputes.stream().map(disputeMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public DisputeResponse getDisputeDetail(String disputeId) {
        Dispute dispute =
                disputeRepository.findById(disputeId).orElseThrow(() -> new AppException(ErrorCode.DISPUTE_NOT_FOUND));

        return disputeMapper.toResponse(dispute);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public DisputeResponse approveDispute(String disputeId) {
        Dispute dispute =
                disputeRepository.findById(disputeId).orElseThrow(() -> new AppException(ErrorCode.DISPUTE_NOT_FOUND));

        if (dispute.getStatus() != DisputeStatus.UNDER_REVIEW) {
            throw new AppException(ErrorCode.DISPUTE_NOT_FOUND);
        }

        refundService.refundDispute(dispute);

        dispute.setAdminNote("Khiếu nại đã được Quản trị viên phê duyệt và hoàn tiền thành công.");
        dispute.setStatus(DisputeStatus.APPROVED);
        dispute.setResolvedAt(Instant.now());
        disputeRepository.save(dispute);
        actionLogService.log(ActionCode.APPROVE_DISPUTE, dispute.getCreatedBy(), "Dispute", disputeId);

        return disputeMapper.toResponse(dispute);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public DisputeResponse rejectDispute(String disputeId) {
        Dispute dispute =
                disputeRepository.findById(disputeId).orElseThrow(() -> new AppException(ErrorCode.DISPUTE_NOT_FOUND));

        if (dispute.getStatus() != DisputeStatus.UNDER_REVIEW) {
            throw new AppException(ErrorCode.DISPUTE_NOT_FOUND);
        }

        dispute.setAdminNote("Khiếu nại bị bác bỏ do bằng chứng chưa đủ căn cứ xác minh.");
        dispute.setStatus(DisputeStatus.REJECTED);
        dispute.setResolvedAt(Instant.now());
        disputeRepository.save(dispute);
        actionLogService.log(ActionCode.REJECT_DISPUTE, dispute.getCreatedBy(), "Dispute", disputeId);

        return disputeMapper.toResponse(dispute);
    }
}
