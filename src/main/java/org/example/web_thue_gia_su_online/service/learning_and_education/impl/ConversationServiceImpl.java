package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ConversationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.example.web_thue_gia_su_online.enums.ConversationStatus;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.ConversationMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ConversationRepository;
import org.example.web_thue_gia_su_online.repository.tutoring.TutorProfileRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.ConversationService;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationServiceImpl implements ConversationService {

    ConversationRepository conversationRepository;
    UserRepository userRepository;
    TutorProfileRepository tutorProfileRepository;
    ConversationMapper conversationMapper;

    private String getCurrentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {
            throw new AccessDeniedException("Bạn chưa đăng nhập");
        }

        try {
            return authentication.getName();
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "JWT subject phải là UUID của người dùng",
                    exception
            );
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public ConversationResponse create(
            ConversationCreationRequest request) {

        String studentId = getCurrentUserId();

        if (studentId.equals(request.getTutorId())) {
            throw new IllegalArgumentException(
                    "Không thể tạo cuộc trò chuyện với chính mình"
            );
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Không tìm thấy học viên"
                        ));

        User tutor = userRepository.findById(request.getTutorId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Không tìm thấy gia sư"
                        ));

        // Chỉ cho phép nhắn tin với người có hồ sơ gia sư.
        if (!tutorProfileRepository.existsById(tutor.getId())) {
            throw new IllegalArgumentException(
                    "Người dùng này chưa có hồ sơ gia sư"
            );
        }

        // Mở lại cuộc trò chuyện đang thương lượng hoặc đã thống nhất.
        var existing = conversationRepository
                .findByStudent_IdAndTutor_IdOrderByUpdatedAtDesc(
                        studentId, tutor.getId()
                );

        if (existing.isPresent()) {
            Conversation conversation = existing.get();

            if (conversation.getStatus() == ConversationStatus.DECLINED
                    || conversation.getStatus() == ConversationStatus.AGREED) {
                return conversationMapper.toResponse(conversation);
            }
        }

        Conversation conversation = Conversation.builder()
                .student(student)
                .tutor(tutor)
                .status(ConversationStatus.NEGOTIATING)
                .build();

        return conversationMapper.toResponse(
                conversationRepository.save(conversation)
        );
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('TUTOR', 'STUDENT')")
    public List<ConversationResponse> getMyConversations() {
        String userId = getCurrentUserId();

        List<Conversation> conversations =
                conversationRepository.findAllByStudent_IdOrderByUpdatedAtDesc(
                        userId
                );

        // Nếu người đăng nhập là gia sư, lấy các cuộc trò chuyện của gia sư.
        if (conversations.isEmpty()) {
            conversations =
                    conversationRepository.findAllByTutor_IdOrderByUpdatedAtDesc(
                            userId
                    );
        }

        return conversations.stream()
                .map(conversationMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public ConversationResponse getById(String id) {
        Conversation conversation = getConversation(id);
        return conversationMapper.toResponse(conversation);
    }

//    @Override
//    @Transactional
//    public ConversationResponse agreeAsTutor(String id) {
//        Conversation conversation = getConversation(id);
//        String userId = getCurrentUserId();
//
//        if (!conversation.getTutor().getId().equals(userId)) {
//            throw new AccessDeniedException(
//                    "Chỉ gia sư trong cuộc trò chuyện mới được xác nhận"
//            );
//        }
//
//        ensureNegotiating(conversation);
//
//        conversation.setTutorAgreedAt(Instant.now());
//        updateAgreementStatus(conversation);
//
//        return conversationMapper.toResponse(conversation);
//    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public ConversationResponse agreeAsStudent(String id) {
        Conversation conversation = getConversation(id);
        String userId = getCurrentUserId();

        checkParticipant(conversation, userId);

        ensureNegotiating(conversation);

        conversation.setStudentAgreedAt(Instant.now());
        conversation.setStatus(ConversationStatus.AGREED);

        return conversationMapper.toResponse(conversation);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('STUDENT')")
    public ConversationResponse declineAsStudent(String id) {
        Conversation conversation = getConversation(id);
        String userId = getCurrentUserId();

        checkParticipant(conversation, userId);
        ensureNegotiating(conversation);

        conversation.setStatus(ConversationStatus.DECLINED);
        conversation.setStudentAgreedAt(null);

        return conversationMapper.toResponse(conversation);
    }

    private Conversation getConversation(String id) {
        return conversationRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Không tìm thấy cuộc trò chuyện"
                        ));
    }

    private void checkParticipant(
            Conversation conversation,
            String userId) {

        boolean isStudent =
                conversation.getStudent().getId().equals(userId);

        boolean isTutor =
                conversation.getTutor().getId().equals(userId);

        if (!isStudent && !isTutor) {
            throw new AccessDeniedException(
                    "Bạn không thuộc cuộc trò chuyện này"
            );
        }
    }

    private void ensureNegotiating(Conversation conversation) {
        if (conversation.getStatus() != ConversationStatus.NEGOTIATING) {
            throw new IllegalStateException(
                    "Cuộc trò chuyện không còn ở trạng thái thương lượng"
            );
        }
    }
}