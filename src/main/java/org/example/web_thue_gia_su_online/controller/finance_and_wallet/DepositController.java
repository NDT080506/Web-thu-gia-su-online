package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import java.security.Principal;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.DepositRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.DepositResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.DepositService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/wallet/deposits")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepositController {

    DepositService depositService;

    @PostMapping
    public ApiResponse<DepositResponse> createDeposit(@Valid @RequestBody DepositRequest request, Principal principal) {

        DepositResponse result = depositService.createDeposit(principal.getName(), request);

        return ApiResponse.<DepositResponse>builder().result(result).build();
    }

    @PostMapping("/{referenceCode}/confirm")
    public ApiResponse<DepositResponse> confirmDeposit(@PathVariable String referenceCode, Principal principal) {

        DepositResponse result = depositService.confirmDeposit(principal.getName(), referenceCode);

        return ApiResponse.<DepositResponse>builder().result(result).build();
    }
}
