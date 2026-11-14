package org.example.web_thue_gia_su_online.service.learning_and_education;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrokeragePackageService {

    BrokeragePackageResponse create(BrokeragePackageCreationRequest request);

    BrokeragePackageResponse getById(String id);

    Page<BrokeragePackageResponse> getAll(Pageable pageable);

    Page<BrokeragePackageResponse> getPublicActivePackages(Pageable pageable);

    Page<BrokeragePackageResponse> getByTutorId(String tutorId, Pageable pageable);

    BrokeragePackageResponse update(String id, BrokeragePackageUpdateRequest request);

    void delete(String id);
}
