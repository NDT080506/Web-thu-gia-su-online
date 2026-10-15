package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BookingUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BookingResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Booking;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "brokeragePackage.id", source = "packageId")
    @Mapping(target = "student.id", source = "studentId")
    @Mapping(target = "tutor.id", source = "tutorId")
    Booking toEntity(BookingCreationRequest request);

    @Mapping(target = "packageId", source = "brokeragePackage.id")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "tutorId", source = "tutor.id")
    BookingResponse toResponse(Booking entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Booking entity, BookingUpdateRequest request);
}
