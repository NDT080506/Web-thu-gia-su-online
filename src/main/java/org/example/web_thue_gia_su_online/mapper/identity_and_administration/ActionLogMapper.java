package org.example.web_thue_gia_su_online.mapper.identity_and_administration;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.ActionLogCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.ActionLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActionLogMapper {

    @Mapping(target = "admin.id", source = "adminId")
    @Mapping(target = "targetUser.id", source = "targetUserId")
    ActionLog toEntity(ActionLogCreationRequest request);

    @Mapping(target = "adminId", source = "admin.id")
    @Mapping(target = "targetUserId", source = "targetUser.id")
    ActionLogResponse toResponse(ActionLog entity);
}
