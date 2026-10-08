package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ServicePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ServicePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ServicePackageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.ServicePackage;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.ServicePackageMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.ServicePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.ServicePackageService;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServicePackageServiceImpl implements ServicePackageService {

    ServicePackageRepository servicePackageRepository;
    ServicePackageMapper servicePackageMapper;

    @Override
    public ServicePackageResponse createServicePackage(ServicePackageCreationRequest request) {

        ServicePackage servicePackage = servicePackageMapper.toServicePackage(request);
        servicePackage.setCreatedAt(Instant.now());
        servicePackage.setUpdateAt(Instant.now());

        return servicePackageMapper.toServicePackageResponse(servicePackageRepository.save(servicePackage));
    }

    @Override
    public List<ServicePackageResponse> getServicePackages() {
        return servicePackageRepository.findAll().stream()
                .map(servicePackageMapper::toServicePackageResponse)
                .toList();
    }

    @Override
    public ServicePackageResponse getServicePackage(String id) {

        ServicePackage servicePackage = servicePackageRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Service package not found"));

        return servicePackageMapper.toServicePackageResponse(servicePackage);
    }

    @Override
    public ServicePackageResponse updateServicePackage(ServicePackageUpdateRequest request, String id) {

        ServicePackage servicePackage = servicePackageRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Service package not found"));

        servicePackageMapper.updateServicePackage(servicePackage, request);
        servicePackage.setUpdateAt(Instant.now());

        return servicePackageMapper.toServicePackageResponse(servicePackageRepository.save(servicePackage));
    }

    @Override
    public void deleteServicePackage(String id) {

        ServicePackage servicePackage = servicePackageRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Service package not found"));

        servicePackageRepository.delete(servicePackage);
    }
}
