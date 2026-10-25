package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Message;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.MessageMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ConversationRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.MessageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.MessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    ConversationRepository conversationRepository;
    UserRepository userRepository;
    MessageMapper messageMapper;

    private String getCurrentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || authentication.getName().isBlank()) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "User is not authenticated");
        }

        try {
            return authentication.getName();
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "JWT subject must contain the user's UUID", exception);
        }
    }

    private void checkParticipant(
            Conversation conversation, String currentUserId) {

        boolean isStudent = conversation.getStudent()
                .getId().equals(currentUserId);

        boolean isTutor = conversation.getTutor()
                .getId().equals(currentUserId);

        if (!isStudent && !isTutor) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "User is not a participant of this conversation");
        }
    }

//    @Override
//    @Transactional
//    public MessageResponse create(MessageCreationRequest request) {
//        Conversation conversation = conversationRepository.findById(request.getConversationId())
//                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));
//
//        String currentSenderId = getCurrentUserId();
//
//        checkParticipant(conversation, currentSenderId);
//
//        User sender = userRepository.findById(currentSenderId)
//                .orElseThrow(() ->
//                        new AppException(ErrorCode.USER_NOT_FOUND));
//
//        Message message = Message.builder()
//                .conversation(conversation)
//                .sender(sender)
//                .content(request.getContent())
//                .build();
//
//        return messageMapper.toResponse(messageRepository.save(message));
//    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'ROLE', 'STUDENT')")
    public MessageResponse createFromWebSocket(String conversationId, String content, String senderId) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message content must not be blank");
        }

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        // Xác minh người gửi thuộc cuộc trò chuyện.
        checkParticipant(conversation, senderId);

        User sender = userRepository.findById(senderId)
                .orElseThrow(() ->
                        new AppException(ErrorCode.USER_NOT_FOUND));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(content.trim())
                .build();

        Message savedMessage = messageRepository.save(message);

        return messageMapper.toResponse(savedMessage);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'ROLE', 'STUDENT')")
    public MessageResponse update(String id, MessageUpdateRequest request) {
        String currentSenderId = getCurrentUserId();

        Message message = messageRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getSender().getId().equals(currentSenderId)){
            throw new org.springframework.security.access.AccessDeniedException(
                    "You cannot edit another user's message");
        }

        messageMapper.updateEntity(message, request);
        return messageMapper.toResponse(messageRepository.save(message));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'ROLE', 'STUDENT')")
    public void delete(String id) {
        String currentSenderId = getCurrentUserId();

        Message message = messageRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getSender().getId().equals(currentSenderId)){
            throw new org.springframework.security.access.AccessDeniedException(
                    "You cannot delete another user's message");
        }

        messageRepository.delete(message);
    }
}
