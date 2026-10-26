package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.example.web_thue_gia_su_online.enums.BookingStatus;
import org.example.web_thue_gia_su_online.enums.ConversationStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BookingMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BrokeragePackageRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ConversationRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.BookingService;
import org.springframework.security.access.AccessDeniedException;
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
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    BrokeragePackageRepository packageRepository;
    UserRepository userRepository;
    ConversationRepository conversationRepository;
    BookingMapper bookingMapper;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public BookingResponse create(BookingCreationRequest request) {
        // 1. Lấy studentId từ JWT, không tin frontend
        String studentId = getCurrentUserId();
        User student = userRepository.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. Kiểm tra package tồn tại và đang active
        BrokeragePackage pkg = packageRepository
                .findById(request.getPackageId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!pkg.isActive()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 3. Tutor từ package, không từ client
        User tutor = pkg.getTutor();
        if (tutor == null) {
            throw new AppException(ErrorCode.BOOKING_NOT_FOUND);
        }

        // 4. Kiểm tra Conversation tồn tại, thuộc đúng student và tutor, và đã AGREED
        Conversation conversation = conversationRepository
                .findByStudent_IdAndTutor_IdOrderByUpdatedAtDesc(studentId, tutor.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        if (conversation.getStatus() != ConversationStatus.AGREED) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 5. Tính giá từ package price, không tin totalPrice từ client
        long totalPrice = pkg.getPrice();
        long platformFee = totalPrice * pkg.getPlatformFeePercent() / 100;
        long tutorIncome = totalPrice - platformFee;

        // 6. Tạo Booking liên kết với Conversation
        Booking booking = Booking.builder()
                .brokeragePackage(pkg)
                .student(student)
                .tutor(tutor)
                .conversation(conversation)
                .status(BookingStatus.PENDING_PAYMENT)
                .totalPrice(totalPrice)
                .platformFee(platformFee)
                .tutorIncome(tutorIncome)
                .build();

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR')")
    public BookingResponse getById(String id) {
        Booking booking =
                bookingRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // Resource-level authorization: chỉ student/tutor thuộc booking mới được xem
        String currentUserId = getCurrentUserId();
        boolean isStudent = booking.getStudent().getId().equals(currentUserId);
        boolean isTutor =
                booking.getTutor() != null && booking.getTutor().getId().equals(currentUserId);

        if (!isStudent && !isTutor) {
            throw new AccessDeniedException("Bạn không có quyền xem booking này");
        }

        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('STUDENT')")
    public List<BookingResponse> getByMyId() {
        String studentId = getCurrentUserId();
        return bookingRepository.findAllByStudentId(studentId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('TUTOR')")
    public List<BookingResponse> getByTutorId() {
        String tutorId = getCurrentUserId();
        return bookingRepository.findAllByTutorId(tutorId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getByStudentId(String studentId) {
        return bookingRepository.findAllByStudentId(studentId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getByTutorId(String tutorId) {
        return bookingRepository.findAllByTutorId(tutorId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public BookingResponse update(String id) {
        Booking booking =
                bookingRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // Resource-level authorization: chỉ student của booking mới được hủy
        String currentUserId = getCurrentUserId();
        if (!booking.getStudent().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không có quyền hủy booking này");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }
}
