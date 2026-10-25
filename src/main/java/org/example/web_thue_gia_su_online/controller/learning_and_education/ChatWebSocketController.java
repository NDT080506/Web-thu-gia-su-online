package org.example.web_thue_gia_su_online.controller.learning_and_education;


import java.security.Principal;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.MessageService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;

@Controller
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatWebSocketController {

    MessageService messageService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Valid MessageCreationRequest request,
            Principal principal) {

        if (principal == null) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Unauthenticated user");
        }

        String senderId = principal.getName();

        MessageResponse response = messageService.createFromWebSocket(
                request.getConversationId(),
                request.getContent(),
                senderId
        );

        String destination = "/topic/conversations/"
                + request.getConversationId();

        // Chỉ phát sau khi service lưu tin nhắn thành công.
        messagingTemplate.convertAndSend(destination, response);
    }
}
