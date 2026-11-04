package org.example.web_thue_gia_su_online.service.disputing_and_reporting;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;

public interface DisputeService {

    public DisputeResponse createDispute(DisputeCreationRequest request);

    public List<DisputeResponse> getMyDisputes();

    public DisputeResponse getMyDispute(String disputeId);

    public void cancelDispute(String disputeId);

    public List<DisputeResponse> getAllDisputes(DisputeStatus status);

    public DisputeResponse getDisputeDetail(String disputeId);

    public DisputeResponse approveDispute(String disputeId);

    public DisputeResponse rejectDispute(String disputeId);
}
