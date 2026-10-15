package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BookingMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BrokeragePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.BookingService;
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
    public BookingResponse create(BookingCreationRequest request) {
        BrokeragePackage pkg = packageRepository
                .findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        var student = userRepository
                .findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        var tutor = request.getTutorId() != null
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
                .status("pending_payment")
                .totalPrice(totalPrice)
                .platformFee(platformFee)
                .tutorIncome(tutorIncome)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse getById(String id) {
        return bookingRepository
                .findById(id)
                .map(bookingMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    public List<BookingResponse> getByStudentId(String studentId) {
        return bookingRepository.findByStudentId(studentId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> getByTutorId(String tutorId) {
        return bookingRepository.findByTutorId(tutorId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public BookingResponse update(String id, BookingUpdateRequest request) {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        bookingMapper.updateEntity(booking, request);
        booking.setUpdatedAt(Instant.now());
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }
}
