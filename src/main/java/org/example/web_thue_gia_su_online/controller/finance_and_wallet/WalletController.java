package org.example.web_thue_gia_su_online.controller.finance_and_wallet;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WalletService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/wallets")
public class WalletController {

    WalletService walletService;

//    @PostMapping("/{userId}")
//    ApiResponse<WalletResponse> createWallet(
//            @RequestBody @Valid WalletCreationRequest request, @PathVariable String userId) {
//        return ApiResponse.<WalletResponse>builder()
//                .result(walletService.createWallet(request, userId))
//                .build();
//    }

    @GetMapping("/{id}")
    ApiResponse<WalletResponse> getWallet(@PathVariable String id) {
        return ApiResponse.<WalletResponse>builder()
                .result(walletService.getWallet(id))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<WalletResponse> getWalletByUserId(@PathVariable String userId) {
        return ApiResponse.<WalletResponse>builder()
                .result(walletService.getWalletByUserId(userId))
                .build();
    }

    @GetMapping
    ApiResponse<List<WalletResponse>> getWallets() {
        return ApiResponse.<List<WalletResponse>>builder()
                .result(walletService.getWallets())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<WalletResponse> updateWallet(@PathVariable String id, @RequestBody @Valid WalletUpdateRequest request) {
        return ApiResponse.<WalletResponse>builder()
                .result(walletService.updateWallet(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteWallet(@PathVariable String id) {
        walletService.deleteWallet(id);
        return ApiResponse.<Void>builder().message("Wallet has been deleted").build();
    }
}
