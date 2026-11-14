package org.example.web_thue_gia_su_online.service.identity_and_administration.impl;

import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateStatusRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.ActionCode;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.UserMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.authentication.EmailVerificationService;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.example.web_thue_gia_su_online.service.identity_and_administration.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    EmailVerificationService emailVerificationService;
    ActionLogService actionLogService;

    @Override
    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (userRepository.existsByDisplayName(request.getDisplayName())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerifiedAt(null);

        try {
            user = userRepository.saveAndFlush(user);
            emailVerificationService.sendVerificationEmail(user);
            actionLogService.log(ActionCode.CREATE_USER, user, "User", user.getId());
        } catch (DataIntegrityViolationException e) {
            log.error("DataIntegrityViolationException when creating user: {}", e.getMessage());
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findResponses(pageable);
    }

    @Override
    public Page<UserResponse> getTutors(Pageable pageable) {
        return userRepository.findTutorResponses(pageable);
    }

    @Override
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR', 'ADMIN')")
    public UserResponse getMyUser() {

        String id = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
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
        userRepository.save(user);
        actionLogService.log(ActionCode.UPDATE_USER, user, "User", id);

        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUserAccountStatus(UserUpdateStatusRequest request, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setStatus(request.getStatus());
        userRepository.save(user);
        actionLogService.log(ActionCode.UPDATE_USER_STATUS, user, "User", id);

        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TUTOR')")
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
        actionLogService.log(ActionCode.DELETE_USER, user, "User", id);
    }
}
