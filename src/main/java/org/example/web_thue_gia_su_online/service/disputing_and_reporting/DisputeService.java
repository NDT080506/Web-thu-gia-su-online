package org.example.web_thue_gia_su_online.service.disputing_and_reporting;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;

public interface DisputeService {

    DisputeResponse createDispute(DisputeCreationRequest request, String orderId, String raisedById, String sessionId);

    DisputeResponse getDispute(String id);

    List<DisputeResponse> getDisputes();

    DisputeResponse updateDispute(DisputeUpdateRequest request, String id, String adminId);

    void deleteDispute(String id);
}