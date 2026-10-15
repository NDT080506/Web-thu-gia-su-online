package org.example.web_thue_gia_su_online.mapper.tutoring;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorProfileCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorProfileUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.TutorProfileResponse;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TutorProfileMapper {

    TutorProfile toTutorProfile(TutorProfileCreationRequest request);

    TutorProfileResponse toTutorProfileResponse(TutorProfile tutorProfile);

    void updateTutorProfileResponse(@MappingTarget TutorProfile tutorProfile, TutorProfileUpdateRequest request);
}
