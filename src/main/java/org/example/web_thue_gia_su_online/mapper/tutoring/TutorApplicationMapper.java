package org.example.web_thue_gia_su_online.mapper.tutoring;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TutorApplicationMapper {

    TutorApplication toTutorApplication(TutorApplicationCreationRequest request);

    TutorApplicationResponse toTutorApplicationResponse(TutorApplication tutorApplication);
}
