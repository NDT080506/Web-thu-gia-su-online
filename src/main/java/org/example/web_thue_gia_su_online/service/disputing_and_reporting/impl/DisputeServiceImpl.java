package org.example.web_thue_gia_su_online.service.disputing_and_reporting.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Session;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;
import org.example.web_thue_gia_su_online.mapper.disputing_and_reporting.DisputeMapper;
import org.example.web_thue_gia_su_online.repository.disputing_and_reporting.DisputeRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.OrderRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.SessionRepository;
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
    OrderRepository orderRepository;
    SessionRepository sessionRepository;
    UserRepository userRepository;
    DisputeMapper disputeMapper;

    @Override
    public DisputeResponse createDispute(
            DisputeCreationRequest request, String orderId, String raisedById, String sessionId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        User raisedBy = userRepository.findById(raisedById).orElseThrow(() -> new RuntimeException("User not found"));

        Session session = null;
        if (sessionId != null && !sessionId.isBlank()) {
            session = sessionRepository.findById(sessionId).orElse(null);
        }

        Dispute dispute = disputeMapper.toDispute(request);
        dispute.setOrder(order);
        dispute.setRaisedBy(raisedBy);
        dispute.setSession(session);
        dispute.setStatus(DisputeStatus.PENDING);
        dispute.setCreatedAt(Instant.now());
        dispute.setUpdateAt(Instant.now());

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
    public DisputeResponse updateDispute(DisputeUpdateRequest request, String id, String adminId) {
        Dispute dispute = disputeRepository.findById(id).orElseThrow(() -> new RuntimeException("Dispute not found"));

        if (adminId != null && !adminId.isBlank()) {
            User admin = userRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
            dispute.setAdmin(admin);
        }

        disputeMapper.updateDispute(dispute, request);
        dispute.setUpdateAt(Instant.now());

        return disputeMapper.toDisputeResponse(disputeRepository.save(dispute));
    }

    @Override
    public void deleteDispute(String id) {
        Dispute dispute = disputeRepository.findById(id).orElseThrow(() -> new RuntimeException("Dispute not found"));

        disputeRepository.delete(dispute);
    }
}