package org.example.web_thue_gia_su_online.service.tutoring;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorProfileUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorProfileService {

    public TutorProfile createTutorProfile(User user, TutorApplication tutorApplication);

    public Page<TutorProfileResponse> getTutorProfiles(Pageable pageable);

    public TutorProfileResponse getMyTutorProfile();

    public TutorProfileResponse getTutorProfile(String userId);

    public TutorProfileResponse updateMyTutorProfile(TutorProfileUpdateRequest request);

    public TutorProfileResponse updateTutorProfile(TutorProfileUpdateRequest request, String userId);

    public void deleteTutorProfile(String userId);
}
