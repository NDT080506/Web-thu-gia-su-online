package org.example.web_thue_gia_su_online.service.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    MessageResponse createFromWebSocket(String conversationId, String content, String senderId);

    MessageResponse sendMessage(MessageCreationRequest request);

    Page<MessageResponse> getMessagesByConversation(String conversationId, Pageable pageable);

    MessageResponse update(String id, MessageUpdateRequest request);

    void delete(String id);
}
