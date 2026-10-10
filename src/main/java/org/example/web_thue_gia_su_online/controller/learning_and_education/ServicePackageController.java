package org.example.web_thue_gia_su_online.controller.learning_and_education;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ServicePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.ServicePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.ServicePackageResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.ServicePackageService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/servicepackages")
public class ServicePackageController {

    ServicePackageService servicePackageService;

    @PostMapping
    ApiResponse<ServicePackageResponse> createServicePackage(
            @RequestBody @Valid ServicePackageCreationRequest request) {
        return ApiResponse.<ServicePackageResponse>builder()
                .result(servicePackageService.createServicePackage(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ServicePackageResponse>> getServicePackages() {
        return ApiResponse.<List<ServicePackageResponse>>builder()
                .result(servicePackageService.getServicePackages())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ServicePackageResponse> getServicePackage(@PathVariable String id) {
        return ApiResponse.<ServicePackageResponse>builder()
                .result(servicePackageService.getServicePackage(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ServicePackageResponse> updateServicePackage(
            @PathVariable String id, @RequestBody @Valid ServicePackageUpdateRequest request) {
        return ApiResponse.<ServicePackageResponse>builder()
                .result(servicePackageService.updateServicePackage(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteServicePackage(@PathVariable String id) {
        servicePackageService.deleteServicePackage(id);

        return ApiResponse.<Void>builder()
                .message("Service package has been deleted")
                .build();
    }
}
