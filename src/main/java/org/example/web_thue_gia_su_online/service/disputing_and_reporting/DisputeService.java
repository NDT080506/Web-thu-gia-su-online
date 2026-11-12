package org.example.web_thue_gia_su_online.service.disputing_and_reporting;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.enums.disputing_and_reporting.DisputeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisputeService {

    public DisputeResponse createDispute(DisputeCreationRequest request);

    public Page<DisputeResponse> getMyDisputes(Pageable pageable);

    public DisputeResponse getMyDispute(String disputeId);

    public void cancelDispute(String disputeId);

    public Page<DisputeResponse> getAllDisputes(DisputeStatus status, Pageable pageable);

    public DisputeResponse getDisputeDetail(String disputeId);

    public DisputeResponse approveDispute(String disputeId);

    public DisputeResponse rejectDispute(String disputeId);
}
