package org.example.web_thue_gia_su_online.configuration;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Conversation;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ConversationRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    JwtDecoder jwtDecoder;
    ConversationRepository conversationRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticate(accessor);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            authorizeSubscription(accessor);
        }

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            authorizeSend(accessor);
        }

        return message;
    }

    private void authenticate(StompHeaderAccessor accessor) {
        String authorization = accessor.getFirstNativeHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AccessDeniedException("Missing bearer token");
        }

        String token = authorization.substring(7);
        Jwt jwt = jwtDecoder.decode(token);

        String userId;
        try {
            userId = jwt.getSubject();
        } catch (IllegalArgumentException exception) {
            throw new AccessDeniedException("JWT subject is not a valid user");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, java.util.List.of());

        accessor.setUser(authentication);
    }

    private void authorizeSubscription(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        Authentication authentication = (Authentication) accessor.getUser();

        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Chỉ cho phép đăng ký destination:
        // /topic/conversations/{conversationId}
        String prefix = "/topic/conversations/";

        if (destination == null || !destination.startsWith(prefix)) {
            throw new AccessDeniedException("Subscription destination not allowed");
        }

        String conversationId;
        try {
            conversationId = destination.substring(prefix.length());
        } catch (IllegalArgumentException exception) {
            throw new AccessDeniedException("Invalid conversation ID");
        }

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new AppException(ErrorCode.CONVERSATION_NOT_FOUND));

        String currentUserId = authentication.getName();

        boolean isStudent = conversation.getStudent().getId().equals(currentUserId);

        boolean isTutor = conversation.getTutor().getId().equals(currentUserId);

        if (!isStudent && !isTutor) {
            throw new AccessDeniedException("You are not a participant of this conversation");
        }
    }

    private void authorizeSend(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        Authentication authentication = (Authentication) accessor.getUser();

        if (authentication == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Chỉ cho phép gửi đến /app/chat.send
        if (destination == null || !destination.startsWith("/app/chat")) {
            return; // Không kiểm tra các destination khác
        }

        // Người dùng đã được xác thực qua CONNECT
        // Việc kiểm tra participant được thực hiện trong MessageService
    }
}
