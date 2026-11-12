package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WithdrawalRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WithdrawalResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WithdrawalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/withdrawals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawalController {

    WithdrawalService withdrawalService;

    @PostMapping
    public ApiResponse<WithdrawalResponse> createWithdrawal(@Valid @RequestBody WithdrawalRequest request) {

        return ApiResponse.<WithdrawalResponse>builder()
                .result(withdrawalService.createWithdrawal(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<Page<WithdrawalResponse>> getMyWithdrawals(Pageable pageable) {
        return ApiResponse.<Page<WithdrawalResponse>>builder()
                .result(withdrawalService.getMyWithdrawals(pageable))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<WithdrawalResponse> getMyWithdrawal(@PathVariable String id) {
        return ApiResponse.<WithdrawalResponse>builder()
                .result(withdrawalService.getMyWithdrawal(id))
                .build();
    }
}
