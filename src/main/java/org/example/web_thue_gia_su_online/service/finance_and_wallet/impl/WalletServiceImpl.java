package org.example.web_thue_gia_su_online.service.finance_and_wallet.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.finance_and_wallet.WalletUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.WalletResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.finance_and_wallet.WalletMapper;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WalletService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    private boolean isAdmin() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getAuthorities().stream()
                .anyMatch(a ->
                        Objects.equals(a.getAuthority(), "ADMIN") || Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
    }

    @Override
    @Transactional
    public Wallet createWallet(User user, WalletType type) {

        if (user != null && walletRepository.existsByUser_IdAndType(user.getId(), type)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Instant now = Instant.now();
        Wallet wallet = Wallet.builder()
                .user(user)
                .type(type)
                .availableBalance(0)
                .holdingBalance(0)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return walletRepository.save(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TUTOR')")
    public WalletResponse getWallet(String id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        // Authorization: chỉ owner hoặc admin mới được xem
        if (!isAdmin() && !wallet.getUser().getId().equals(getCurrentUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return walletMapper.toWalletResponse(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TUTOR')")
    public WalletResponse getWalletByUserId(String userId) {
        // Authorization: chỉ owner hoặc admin mới được xem
        if (!isAdmin() && !userId.equals(getCurrentUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Wallet wallet =
                walletRepository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        return walletMapper.toWalletResponse(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<WalletResponse> getWallets() {
        return walletRepository.findAll().stream()
                .map(walletMapper::toWalletResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public WalletResponse updateWallet(WalletUpdateRequest request, String id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        walletMapper.updateWallet(wallet, request);
        wallet.setUpdatedAt(Instant.now());

        return walletMapper.toWalletResponse(walletRepository.save(wallet));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWallet(String id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        walletRepository.delete(wallet);
    }
}
