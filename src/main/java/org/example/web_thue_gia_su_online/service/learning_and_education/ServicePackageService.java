package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ServicePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ServicePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ServicePackageResponse;

public interface ServicePackageService {
    public ServicePackageResponse createServicePackage(ServicePackageCreationRequest request);

    public List<ServicePackageResponse> getServicePackages();

    public ServicePackageResponse getServicePackage(String id);

    public ServicePackageResponse updateServicePackage(ServicePackageUpdateRequest request, String id);

    public void deleteServicePackage(String id);
}
