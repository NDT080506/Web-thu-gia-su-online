package org.example.web_thue_gia_su_online.controller.disputing_and_reporting;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.service.disputing_and_reporting.DisputeService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/disputes")
public class DisputeController {

    DisputeService disputeService;

    @PostMapping
    public ApiResponse<DisputeResponse> createDispute(@Valid @RequestBody DisputeCreationRequest request) {

        return ApiResponse.<DisputeResponse>builder()
                .result(disputeService.createDispute(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<DisputeResponse>> getMyDisputes() {
        return ApiResponse.<List<DisputeResponse>>builder()
                .result(disputeService.getMyDisputes())
                .build();
    }

    @GetMapping("/{disputeId}")
    public ApiResponse<DisputeResponse> getMyDispute(@PathVariable String disputeId) {
        return ApiResponse.<DisputeResponse>builder()
                .result(disputeService.getMyDispute(disputeId))
                .build();
    }

    @PatchMapping("/{disputeId}/cancel")
    public ApiResponse<Void> cancelDispute(@PathVariable String disputeId) {
        disputeService.cancelDispute(disputeId);

        return ApiResponse.<Void>builder()
                .message("Dispute cancelled successfully")
                .build();
    }
}
