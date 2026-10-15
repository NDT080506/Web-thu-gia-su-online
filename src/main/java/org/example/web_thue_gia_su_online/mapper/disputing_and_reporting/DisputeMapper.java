package org.example.web_thue_gia_su_online.mapper.disputing_and_reporting;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DisputeMapper {

    @Mapping(target = "booking.id", source = "bookingId")
    @Mapping(target = "student.id", source = "studentId")
    Dispute toDispute(DisputeCreationRequest request);

    @Mapping(source = "booking.id", target = "bookingId")
    @Mapping(source = "student.id", target = "studentId")
    DisputeResponse toDisputeResponse(Dispute dispute);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDispute(@MappingTarget Dispute dispute, DisputeUpdateRequest request);
}