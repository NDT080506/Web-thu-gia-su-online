package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BrokeragePackageMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BrokeragePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.BrokeragePackageService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrokeragePackageServiceImpl implements BrokeragePackageService {

    BrokeragePackageRepository repository;
    BrokeragePackageMapper mapper;
    UserRepository userRepository;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'TUTOR')")
    public BrokeragePackageResponse create(BrokeragePackageCreationRequest request) {
        BrokeragePackage pkg = mapper.toEntity(request);
        pkg.setActive(true);

        String tutorId = getCurrentUserId();
        User tutor = userRepository.findById(tutorId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        pkg.setTutor(tutor);
        repository.save(pkg);
        return mapper.toResponse(pkg);
    }

    @Override
    @Transactional(readOnly = true)
    public BrokeragePackageResponse getById(String id) {
        BrokeragePackage pkg = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        return mapper.toResponse(pkg);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrokeragePackageResponse> getByTutorId(String tutorId) {
        return repository.findByTutorIdAndActiveTrue(tutorId).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BrokeragePackageResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'TUTOR')")
    public BrokeragePackageResponse update(String id, BrokeragePackageUpdateRequest request) {
        BrokeragePackage pkg = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        // Kiểm tra ownership
        String currentUserId = getCurrentUserId();
        if (pkg.getTutor() != null && !pkg.getTutor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không có quyền sửa gói dịch vụ này");
        }

        mapper.updateEntity(pkg, request);
        repository.save(pkg);
        return mapper.toResponse(pkg);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String id) {
        repository.deleteById(id);
    }
}
