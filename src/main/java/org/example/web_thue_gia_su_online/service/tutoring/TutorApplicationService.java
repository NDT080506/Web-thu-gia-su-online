package org.example.web_thue_gia_su_online.service.tutoring;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;

public interface TutorApplicationService {
    public TutorApplicationResponse createTutorApplication(TutorApplicationCreationRequest request);

    public TutorApplicationResponse getMyTutorApplication();

    public TutorApplicationResponse getTutorApplication(String id);

    public List<TutorApplicationResponse> getTutorApplications();

    public TutorApplicationResponse approveTutorApplication(String id);

    public TutorApplicationResponse rejectTutorApplication(String id);

    public void deleteTutorApplication(String id);

    public void deleteMyTutorApplication();
}
