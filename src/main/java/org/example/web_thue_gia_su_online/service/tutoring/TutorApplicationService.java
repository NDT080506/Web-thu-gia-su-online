package org.example.web_thue_gia_su_online.service.tutoring;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorApplicationService {
    public TutorApplicationResponse createTutorApplication(TutorApplicationCreationRequest request);

    public TutorApplicationResponse getMyTutorApplication();

    public TutorApplicationResponse getTutorApplication(String id);

    public Page<TutorApplicationResponse> getTutorApplications(Pageable pageable);

    public TutorApplicationResponse approveTutorApplication(String id);

    public TutorApplicationResponse rejectTutorApplication(String id);

    public void deleteTutorApplication(String id);

    public void deleteMyTutorApplication();
}
