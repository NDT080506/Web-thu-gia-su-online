package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.SessionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.SessionResponse;

public interface SessionService {
    public SessionResponse startSession(SessionCreationRequest request, String orderId);

    public SessionResponse endSession(String id);

    public List<SessionResponse> getSessions();

    public SessionResponse getSession(String id);

    public void deleteSession(String id);
}
