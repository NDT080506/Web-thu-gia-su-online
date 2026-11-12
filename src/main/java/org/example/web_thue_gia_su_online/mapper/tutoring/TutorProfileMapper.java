package org.example.web_thue_gia_su_online.mapper.tutoring;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorProfileCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorProfileUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TutorProfileMapper {

    TutorProfile toTutorProfile(TutorProfileCreationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userId", source = "user.id")
    TutorProfileResponse toTutorProfileResponse(TutorProfile tutorProfile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTutorProfileResponse(@MappingTarget TutorProfile tutorProfile, TutorProfileUpdateRequest request);
}
