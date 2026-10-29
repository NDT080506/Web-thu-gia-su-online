package org.example.web_thue_gia_su_online.service.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;

public interface BrokeragePackageService {

    BrokeragePackageResponse create(BrokeragePackageCreationRequest request);

    BrokeragePackageResponse getById(String id);

    List<BrokeragePackageResponse> getAll();

    List<BrokeragePackageResponse> getByTutorId(String tutorId);

    BrokeragePackageResponse update(String id, BrokeragePackageUpdateRequest request);

    void delete(String id);
}
