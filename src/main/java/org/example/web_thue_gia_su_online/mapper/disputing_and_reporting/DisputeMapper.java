package org.example.web_thue_gia_su_online.mapper.disputing_and_reporting;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DisputeMapper {

    @Mapping(target = "booking.id", source = "bookingId")
    @Mapping(target = "enrollment.id", source = "enrollmentId")
    Dispute toDispute(DisputeCreationRequest request);

    @Mapping(source = "booking.id", target = "bookingId")
    @Mapping(source = "enrollment.id", target = "enrollmentId")
    @Mapping(source = "createdBy.id", target = "createdBy")
    DisputeResponse toResponse(Dispute dispute);
}
