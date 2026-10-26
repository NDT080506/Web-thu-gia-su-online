package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Message;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.MessageMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ConversationRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.MessageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.MessageService;
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
public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;
    ConversationRepository conversationRepository;
    UserRepository userRepository;
    MessageMapper messageMapper;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    private void checkParticipant(Conversation conversation, String userId) {
        boolean isStudent = conversation.getStudent().getId().equals(userId);
        boolean isTutor = conversation.getTutor().getId().equals(userId);

        if (!isStudent && !isTutor) {
            throw new AccessDeniedException("Bạn không thuộc cuộc trò chuyện này");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR')")
    public MessageResponse createFromWebSocket(String conversationId, String content, String senderId) {
        if (content == null || content.isBlank()) {
            throw new AppException(ErrorCode.MESSAGE_NOT_FOUND);
        }

        // senderId từ WebSocket Controller đã lấy từ Principal — an toàn
        // Nhưng vẫn cần xác minh lại để đảm bảo
        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        checkParticipant(conversation, senderId);

        User sender = userRepository.findById(senderId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(content)
                .build();

        Message savedMessage = messageRepository.save(message);
        return messageMapper.toResponse(savedMessage);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR')")
    public MessageResponse update(String id, MessageUpdateRequest request) {
        String currentUserId = getCurrentUserId();

        Message message =
                messageRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getSender().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không thể sửa tin nhắn của người khác");
        }

        messageMapper.updateEntity(message, request);
        return messageMapper.toResponse(messageRepository.save(message));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR')")
    public void delete(String id) {
        String currentUserId = getCurrentUserId();

        Message message =
                messageRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MESSAGE_NOT_FOUND));

        if (!message.getSender().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không thể xóa tin nhắn của người khác");
        }

        messageRepository.delete(message);
    }
}
