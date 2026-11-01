package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BrokeragePackageMapper {

    BrokeragePackage toEntity(BrokeragePackageCreationRequest request);

    @Mapping(target = "tutorId", source = "tutor.id")
    BrokeragePackageResponse toResponse(BrokeragePackage entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget BrokeragePackage entity, BrokeragePackageUpdateRequest request);
}
