package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.enums.BookingStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BookingMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BrokeragePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.BookingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    BookingMapper bookingMapper;

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public BookingResponse create(BookingCreationRequest request) {
        BrokeragePackage pkg = packageRepository
                .findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        User student = userRepository
                .findById(request.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User tutor = request.getTutorId() != null
                ? userRepository.findById(request.getTutorId()).orElse(null)
                : null;

        long totalPrice = request.getTotalPrice();
        long platformFee = totalPrice * pkg.getPlatformFeePercent() / 100;
        long tutorIncome = totalPrice - platformFee;

        Instant now = Instant.now();
        Booking booking = Booking.builder()
                .brokeragePackage(pkg)
                .student(student)
                .tutor(tutor)
                .subject(request.getSubject())
                .status(BookingStatus.PENDING_PAYMENT)
                .totalPrice(totalPrice)
                .platformFee(platformFee)
                .tutorIncome(tutorIncome)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR')")
    public BookingResponse getById(String id) {
        return bookingRepository
                .findById(id)
                .map(bookingMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public List<BookingResponse> getByMyId() {
        String studentId = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                        .getName();

        return bookingRepository
                .findAllByStudentId(studentId)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('TUTOR')")
    public List<BookingResponse> getByTutorId() {

        String tutorId = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                        .getName();

        return bookingRepository.findAllByTutorId(tutorId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getByStudentId(String studentId) {
        return bookingRepository.findAllByStudentId(studentId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> getByTutorId(String tutorId) {
        return bookingRepository.findAllByTutorId(tutorId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public BookingResponse update(String id) {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        booking.setStatus(BookingStatus.CANCELLED);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }
}
