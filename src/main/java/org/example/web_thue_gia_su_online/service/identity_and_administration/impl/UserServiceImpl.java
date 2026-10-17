package org.example.web_thue_gia_su_online.service.identity_and_administration.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateStatusRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.WalletType;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.UserMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.finance_and_wallet.WalletService;
import org.example.web_thue_gia_su_online.service.identity_and_administration.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    WalletService walletService;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @PreAuthorize("hasRole('GUEST')")
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Mail này đã được sở hữu");
        }

        if (userRepository.existsByDisplayName(request.getDisplayName())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        Instant now = Instant.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        try {
            userRepository.save(user);
            walletService.createWallet(user, WalletType.NORMAL);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR')")
    public UserResponse getMyUser() {

        String id = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                        .getName();

        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TUTOR')")
    public UserResponse updateUser(UserUpdateRequest request, String id) {

        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);
        user.setUpdatedAt(Instant.now());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUserAccountStatus(UserUpdateStatusRequest request, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setStatus(request.getStatus());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TUTOR')")
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
    }
}
