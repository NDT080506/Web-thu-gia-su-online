package org.example.web_thue_gia_su_online.controller.learning_and_education;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.BrokeragePackageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/brokerage-packages")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrokeragePackageController {

    BrokeragePackageService packageService;

    @PostMapping
    public ApiResponse<BrokeragePackageResponse> create(@RequestBody @Valid BrokeragePackageCreationRequest request) {
        return ApiResponse.<BrokeragePackageResponse>builder()
                .result(packageService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<BrokeragePackageResponse>> getAllPublic(Pageable pageable) {
        return ApiResponse.<Page<BrokeragePackageResponse>>builder()
                .result(packageService.getPublicActivePackages(pageable))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BrokeragePackageResponse> getById(@PathVariable String id) {
        return ApiResponse.<BrokeragePackageResponse>builder()
                .result(packageService.getById(id))
                .build();
    }

    @GetMapping("/tutor/{tutorId}")
    public ApiResponse<Page<BrokeragePackageResponse>> getByTutorId(@PathVariable String tutorId, Pageable pageable) {
        return ApiResponse.<Page<BrokeragePackageResponse>>builder()
                .result(packageService.getByTutorId(tutorId, pageable))
                .build();
    }

    @GetMapping("/admin/get-all")
    public ApiResponse<Page<BrokeragePackageResponse>> getAll(Pageable pageable) {
        return ApiResponse.<Page<BrokeragePackageResponse>>builder()
                .result(packageService.getAll(pageable))
                .build();
    }

    @PatchMapping("/admin/{id}")
    public ApiResponse<BrokeragePackageResponse> adminUpdate(
            @PathVariable String id, @RequestBody @Valid BrokeragePackageUpdateRequest request) {
        return ApiResponse.<BrokeragePackageResponse>builder()
                .result(packageService.update(id, request))
                .build();
    }

    @PatchMapping("/tutor/{id}")
    public ApiResponse<BrokeragePackageResponse> tutorUpdate(
            @PathVariable String id, @RequestBody @Valid BrokeragePackageUpdateRequest request) {
        return ApiResponse.<BrokeragePackageResponse>builder()
                .result(packageService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        packageService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Brokerage package deleted successfully")
                .build();
    }
}
