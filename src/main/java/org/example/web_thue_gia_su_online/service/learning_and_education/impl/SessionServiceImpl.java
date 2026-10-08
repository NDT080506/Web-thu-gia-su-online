package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.SessionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.SessionResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Session;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.SessionMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.OrderRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.SessionRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.SessionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionServiceImpl implements SessionService {

    OrderRepository orderRepository;
    SessionRepository sessionRepository;
    SessionMapper sessionMapper;

    @Override
    public SessionResponse startSession(SessionCreationRequest request, String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        Session session = sessionMapper.toSession(request);
        session.setOrder(order);

        Instant now = Instant.now();
        session.setStart_time(now);
        session.setCreated_at(now);

        try {
            sessionRepository.save(session);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }

        return sessionMapper.toSessionResponse(session);
    }

    @Override
    public SessionResponse endSession(String id) {

        Session session = sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));

        session.setEnd_time(Instant.now());

        return sessionMapper.toSessionResponse(sessionRepository.save(session));
    }

    @Override
    public List<SessionResponse> getSessions() {
        return sessionRepository.findAll().stream()
                .map(sessionMapper::toSessionResponse)
                .toList();
    }

    @Override
    public SessionResponse getSession(String id) {
        Session session = sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));

        return sessionMapper.toSessionResponse(session);
    }

    @Override
    public void deleteSession(String id) {
        Session session = sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));

        sessionRepository.delete(session);
    }
}
