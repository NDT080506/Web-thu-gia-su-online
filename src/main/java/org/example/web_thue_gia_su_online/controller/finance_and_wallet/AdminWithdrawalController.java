package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WithdrawalResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WithdrawalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/admin/withdrawals")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminWithdrawalController {

    WithdrawalService withdrawalService;

    @GetMapping
    public ApiResponse<List<WithdrawalResponse>> getAllWithdrawals() {
        return ApiResponse.<List<WithdrawalResponse>>builder()
                .result(withdrawalService.getAllWithdrawals())
                .build();
    }
}
