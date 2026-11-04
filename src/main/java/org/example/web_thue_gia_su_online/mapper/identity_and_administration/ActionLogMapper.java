package org.example.web_thue_gia_su_online.mapper.identity_and_administration;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.ActionLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActionLogMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.displayName", target = "userDisplayName")
    ActionLogResponse toResponse(ActionLog entity);
}
