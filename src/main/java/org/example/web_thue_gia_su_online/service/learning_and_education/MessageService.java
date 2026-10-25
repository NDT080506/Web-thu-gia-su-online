package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;

public interface MessageService {

//    MessageResponse create(MessageCreationRequest request);

    MessageResponse createFromWebSocket(
            String conversationId,
            String content,
            String senderId);

    MessageResponse update(String id, MessageUpdateRequest request);

    void delete(String id);
}
