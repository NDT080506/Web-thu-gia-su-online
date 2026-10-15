package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BrokeragePackageMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BrokeragePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.BrokeragePackageService;
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
    public BrokeragePackageResponse create(BrokeragePackageCreationRequest request) {
        BrokeragePackage pkg = mapper.toEntity(request);
        return mapper.toResponse(repository.save(pkg));
    }

    @Override
    public BrokeragePackageResponse getById(String id) {
        return repository
                .findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Brokerage package not found with id: " + id));
    }

    @Override
    public List<BrokeragePackageResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public BrokeragePackageResponse update(String id, BrokeragePackageUpdateRequest request) {
        BrokeragePackage pkg = repository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Brokerage package not found with id: " + id));
        mapper.updateEntity(pkg, request);
        return mapper.toResponse(repository.save(pkg));
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
