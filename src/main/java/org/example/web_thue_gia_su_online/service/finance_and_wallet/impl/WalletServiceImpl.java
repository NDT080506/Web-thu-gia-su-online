package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.WalletMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WalletService;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletServiceImpl implements WalletService {

    WalletRepository walletRepository;
    UserRepository userRepository;
    WalletMapper walletMapper;

    @Override
    public WalletResponse createWallet(WalletCreationRequest request, String userId) {
        User user = null;
        String targetUserId = userId != null ? userId : request.getUserId();
        if (targetUserId != null && !targetUserId.isBlank()) {
            user = userRepository.findById(targetUserId).orElseThrow(() -> new RuntimeException("User not found"));
        } else if (request.getType() != WalletType.PLATFORM) {
            throw new RuntimeException("User ID is required for non-platform wallets");
        }

        Instant now = Instant.now();
        Wallet wallet = Wallet.builder()
                .user(user)
                .type(request.getType())
                .availableBalance(0)
                .holdingBalance(0)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return walletMapper.toWalletResponse(walletRepository.save(wallet));
    }

    @Override
    public WalletResponse getWallet(String id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));

        return walletMapper.toWalletResponse(wallet);
    }

    @Override
    public WalletResponse getWalletByUserId(String userId) {
        Wallet wallet = walletRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));

        return walletMapper.toWalletResponse(wallet);
    }

    @Override
    public List<WalletResponse> getWallets() {
        return walletRepository.findAll().stream()
                .map(walletMapper::toWalletResponse)
                .toList();
    }

    @Override
    public WalletResponse updateWallet(WalletUpdateRequest request, String id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));

        walletMapper.updateWallet(wallet, request);
        wallet.setUpdatedAt(Instant.now());

        return walletMapper.toWalletResponse(walletRepository.save(wallet));
    }

    @Override
    public void deleteWallet(String id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));

        walletRepository.delete(wallet);
    }
}