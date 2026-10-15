package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Message;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.MessageMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.MessageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.MessageService;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    BookingRepository bookingRepository;
    UserRepository userRepository;
    MessageMapper messageMapper;

    @Override
    public MessageResponse create(MessageCreationRequest request) {
        Booking booking = bookingRepository
                .findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        var sender = userRepository
                .findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = Message.builder()
                .booking(booking)
                .sender(sender)
                .content(request.getContent())
                .createdAt(Instant.now())
                .build();

        return messageMapper.toResponse(messageRepository.save(message));
    }

    @Override
    public List<MessageResponse> getByBookingId(String bookingId) {
        return messageRepository.findByBookingIdOrderByCreatedAtAsc(bookingId).stream()
                .map(messageMapper::toResponse)
                .toList();
    }

    @Override
    public MessageResponse update(Long id, MessageUpdateRequest request) {
        Message message = messageRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));

        messageMapper.updateEntity(message, request);
        return messageMapper.toResponse(messageRepository.save(message));
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }
}
