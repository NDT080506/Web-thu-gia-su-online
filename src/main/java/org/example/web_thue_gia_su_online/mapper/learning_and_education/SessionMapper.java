package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.SessionCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.SessionResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    Session toSession(SessionCreationRequest request);

    SessionResponse toSessionResponse(Session session);
}
