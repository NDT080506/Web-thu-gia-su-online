package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ConversationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ConversationResponse;

public interface ConversationService {
    public ConversationResponse create(ConversationCreationRequest request);

    public List<ConversationResponse> getMyConversations();

    public ConversationResponse getById(String id);

    // public ConversationResponse agreeAsTutor(String id);

    public ConversationResponse agreeAsStudent(String id);

    public ConversationResponse declineAsStudent(String id);
}
