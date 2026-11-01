package org.example.web_thue_gia_su_online.service.tutoring.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.enums.RequestStatus;
import org.example.web_thue_gia_su_online.enums.UserRole;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.tutoring.TutorApplicationMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.tutoring.TutorApplicationRepository;
import org.example.web_thue_gia_su_online.service.tutoring.TutorApplicationService;
import org.example.web_thue_gia_su_online.service.tutoring.TutorProfileService;
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
public class TutorApplicationServiceImpl implements TutorApplicationService {

    UserRepository userRepository;
    TutorApplicationRepository tutorApplicationRepository;
    TutorApplicationMapper tutorApplicationMapper;
    TutorProfileService tutorProfileService;

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    public TutorApplicationResponse createTutorApplication(TutorApplicationCreationRequest request) {
        String userId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        if (tutorApplicationRepository.existsByUserIdAndStatus(userId, RequestStatus.PENDING)) {
            throw new AppException(ErrorCode.TUTOR_APPLICATION_EXISTED);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        TutorApplication tutorApplication = tutorApplicationMapper.toTutorApplication(request);
        tutorApplication.setUser(user);
        Instant now = Instant.now();
        tutorApplication.setCreatedAt(now);
        tutorApplication.setUpdatedAt(now);

        tutorApplicationRepository.save(tutorApplication);

        return tutorApplicationMapper.toTutorApplicationResponse(tutorApplication);
    }

    @Override
    @PreAuthorize("hasAnyRole('STUDENT')")
    public TutorApplicationResponse getMyTutorApplication() {

        String userId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        TutorApplication tutorApplication = tutorApplicationRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_APPLICATION_NOT_FOUND));

        return tutorApplicationMapper.toTutorApplicationResponse(tutorApplication);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TutorApplicationResponse getTutorApplication(String id) {

        TutorApplication tutorApplication = tutorApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_APPLICATION_NOT_FOUND));

        return tutorApplicationMapper.toTutorApplicationResponse(tutorApplication);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<TutorApplicationResponse> getTutorApplications() {
        return tutorApplicationRepository.findAll().stream()
                .map(tutorApplicationMapper::toTutorApplicationResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TutorApplicationResponse approveTutorApplication(String id) {

        TutorApplication tutorApplication = tutorApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_APPLICATION_NOT_FOUND));

        tutorApplication.setStatus(RequestStatus.APPROVED);
        tutorApplication.setUpdatedAt(Instant.now());

        User user = userRepository
                .findById(tutorApplication.getUser().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setRole(UserRole.TUTOR);

        userRepository.save(user);
        tutorApplicationRepository.save(tutorApplication);
        tutorProfileService.createTutorProfile(user, tutorApplication);

        return tutorApplicationMapper.toTutorApplicationResponse(tutorApplication);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TutorApplicationResponse rejectTutorApplication(String id) {

        TutorApplication tutorApplication = tutorApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_APPLICATION_NOT_FOUND));

        tutorApplication.setStatus(RequestStatus.REJECTED);
        tutorApplication.setUpdatedAt(Instant.now());

        tutorApplicationRepository.save(tutorApplication);

        return tutorApplicationMapper.toTutorApplicationResponse(tutorApplication);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteTutorApplication(String id) {

        TutorApplication tutorApplication = tutorApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_APPLICATION_NOT_FOUND));

        tutorApplicationRepository.delete(tutorApplication);
    }

    @Override
    @PreAuthorize("hasRole('STUDENT')")
    @Transactional
    public void deleteMyTutorApplication() {

        String userId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        TutorApplication tutorApplication = tutorApplicationRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_APPLICATION_NOT_FOUND));

        tutorApplicationRepository.delete(tutorApplication);
    }
}
