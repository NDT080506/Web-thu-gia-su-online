package org.example.web_thue_gia_su_online.controller.disputing_and_reporting;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.service.disputing_and_reporting.DisputeService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/disputes")
public class DisputeController {

    DisputeService disputeService;

    @PostMapping("/{orderId}/{raisedById}")
    ApiResponse<DisputeResponse> createDispute(
            @RequestBody @Valid DisputeCreationRequest request,
            @PathVariable String orderId,
            @PathVariable String raisedById,
            @RequestParam(required = false) String sessionId) {
        return ApiResponse.<DisputeResponse>builder()
                .result(disputeService.createDispute(request, orderId, raisedById, sessionId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<DisputeResponse> getDispute(@PathVariable String id) {
        return ApiResponse.<DisputeResponse>builder()
                .result(disputeService.getDispute(id))
                .build();
    }

    @GetMapping
    ApiResponse<List<DisputeResponse>> getDisputes() {
        return ApiResponse.<List<DisputeResponse>>builder()
                .result(disputeService.getDisputes())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<DisputeResponse> updateDispute(
            @PathVariable String id,
            @RequestParam(required = false) String adminId,
            @RequestBody @Valid DisputeUpdateRequest request) {
        return ApiResponse.<DisputeResponse>builder()
                .result(disputeService.updateDispute(request, id, adminId))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteDispute(@PathVariable String id) {
        disputeService.deleteDispute(id);
        return ApiResponse.<Void>builder().message("Dispute has been deleted").build();
    }
}