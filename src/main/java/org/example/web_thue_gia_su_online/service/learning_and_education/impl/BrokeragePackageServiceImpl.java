package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BrokeragePackageMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BrokeragePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.BrokeragePackageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrokeragePackageServiceImpl implements BrokeragePackageService {

    BrokeragePackageRepository repository;
    BrokeragePackageMapper mapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public BrokeragePackageResponse create(BrokeragePackageCreationRequest request) {
        BrokeragePackage pkg = mapper.toEntity(request);
        Instant now = Instant.now();
        pkg.setCreatedAt(now);
        pkg.setUpdatedAt(now);

        return mapper.toResponse(repository.save(pkg));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public BrokeragePackageResponse getById(String id) {
        return repository
                .findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Brokerage package not found with id: " + id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<BrokeragePackageResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TUTOR')")
    public BrokeragePackageResponse update(String id, BrokeragePackageUpdateRequest request) {
        BrokeragePackage pkg = repository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Brokerage package not found with id: " + id));
        mapper.updateEntity(pkg, request);
        return mapper.toResponse(repository.save(pkg));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'TUTOR')")
    public void delete(String id) {
        repository.deleteById(id);
    }
}
