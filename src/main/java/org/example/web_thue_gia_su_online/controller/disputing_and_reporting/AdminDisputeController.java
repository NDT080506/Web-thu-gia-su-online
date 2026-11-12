package org.example.web_thue_gia_su_online.controller.disputing_and_reporting;

import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.enums.disputing_and_reporting.DisputeStatus;
import org.example.web_thue_gia_su_online.service.disputing_and_reporting.DisputeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/admin/disputes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDisputeController {

    DisputeService disputeService;

    @GetMapping
    public ApiResponse<Page<DisputeResponse>> getAllDisputes(
            @RequestParam(required = false) DisputeStatus status, Pageable pageable) {
        return ApiResponse.<Page<DisputeResponse>>builder()
                .result(disputeService.getAllDisputes(status, pageable))
                .build();
    }

    @GetMapping("/{disputeId}")
    public ApiResponse<DisputeResponse> getDisputeDetail(@PathVariable String disputeId) {
        return ApiResponse.<DisputeResponse>builder()
                .result(disputeService.getDisputeDetail(disputeId))
                .build();
    }

    @PatchMapping("/{disputeId}/approve")
    public ApiResponse<DisputeResponse> approveDispute(@PathVariable String disputeId) {
        return ApiResponse.<DisputeResponse>builder()
                .result(disputeService.approveDispute(disputeId))
                .build();
    }

    @PatchMapping("/{disputeId}/reject")
    public ApiResponse<DisputeResponse> rejectDispute(@PathVariable String disputeId) {
        return ApiResponse.<DisputeResponse>builder()
                .result(disputeService.rejectDispute(disputeId))
                .build();
    }
}
