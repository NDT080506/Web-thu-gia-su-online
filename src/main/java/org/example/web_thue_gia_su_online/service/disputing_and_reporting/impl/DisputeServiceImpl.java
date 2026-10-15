package org.example.web_thue_gia_su_online.service.disputing_and_reporting.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;
import org.example.web_thue_gia_su_online.mapper.disputing_and_reporting.DisputeMapper;
import org.example.web_thue_gia_su_online.repository.disputing_and_reporting.DisputeRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.service.disputing_and_reporting.DisputeService;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DisputeServiceImpl implements DisputeService {

    DisputeRepository disputeRepository;
    BookingRepository bookingRepository;
    UserRepository userRepository;
    DisputeMapper disputeMapper;

    @Override
    public DisputeResponse createDispute(DisputeCreationRequest request) {
        Booking booking = bookingRepository
                .findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        User student = userRepository
                .findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Dispute dispute = Dispute.builder()
                .booking(booking)
                .student(student)
                .reason(request.getReason())
                .status(DisputeStatus.PENDING)
                .refundAmount(0)
                .createdAt(Instant.now())
                .build();

        return disputeMapper.toDisputeResponse(disputeRepository.save(dispute));
    }

    @Override
    public DisputeResponse getDispute(String id) {
        Dispute dispute = disputeRepository.findById(id).orElseThrow(() -> new RuntimeException("Dispute not found"));

        return disputeMapper.toDisputeResponse(dispute);
    }

    @Override
    public List<DisputeResponse> getDisputes() {
        return disputeRepository.findAll().stream()
                .map(disputeMapper::toDisputeResponse)
                .toList();
    }

    @Override
    public DisputeResponse updateDispute(DisputeUpdateRequest request, String id) {
        Dispute dispute = disputeRepository.findById(id).orElseThrow(() -> new RuntimeException("Dispute not found"));

        disputeMapper.updateDispute(dispute, request);
        if (request.getStatus() != null && request.getStatus() != DisputeStatus.PENDING) {
            dispute.setResolvedAt(Instant.now());
        }

        return disputeMapper.toDisputeResponse(disputeRepository.save(dispute));
    }

    @Override
    public void deleteDispute(String id) {
        Dispute dispute = disputeRepository.findById(id).orElseThrow(() -> new RuntimeException("Dispute not found"));

        disputeRepository.delete(dispute);
    }
}