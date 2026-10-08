package org.example.web_thue_gia_su_online.service.identity_and_administration;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.TutorProfileCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.TutorProfileUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.TutorProfileResponse;

public interface TutorProfileService {

    public TutorProfileResponse createTutorProfile(TutorProfileCreationRequest request, String userId);

    public List<TutorProfileResponse> getTutorProfiles();

    public TutorProfileResponse getTutorProfile(String id);

    public TutorProfileResponse updateTutorProfile(TutorProfileUpdateRequest request, String id);

    public void deleteTutorProfile(String id);
}
