package org.example.web_thue_gia_su_online.mapper.disputing_and_reporting;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DisputeMapper {

    Dispute toDispute(DisputeCreationRequest request);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "session.id", target = "sessionId")
    @Mapping(source = "raisedBy.id", target = "raisedById")
    @Mapping(source = "admin.id", target = "adminId")
    DisputeResponse toDisputeResponse(Dispute dispute);

    void updateDispute(@MappingTarget Dispute dispute, DisputeUpdateRequest request);
}