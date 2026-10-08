package org.example.web_thue_gia_su_online.mapper.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ServicePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ServicePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ServicePackageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.ServicePackage;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServicePackageMapper {

    ServicePackage toServicePackage(ServicePackageCreationRequest request);

    ServicePackageResponse toServicePackageResponse(ServicePackage servicePackage);

    void updateServicePackage(@MappingTarget ServicePackage servicePackage, ServicePackageUpdateRequest request);
}
