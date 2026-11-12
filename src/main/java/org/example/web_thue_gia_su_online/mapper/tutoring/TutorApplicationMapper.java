package org.example.web_thue_gia_su_online.mapper.tutoring;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TutorApplicationMapper {

    TutorApplication toTutorApplication(TutorApplicationCreationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.displayName")
    @Mapping(target = "userEmail", source = "user.email")
    TutorApplicationResponse toTutorApplicationResponse(TutorApplication tutorApplication);
}
