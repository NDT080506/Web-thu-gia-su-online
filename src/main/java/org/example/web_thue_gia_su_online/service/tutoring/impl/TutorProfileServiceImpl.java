package org.example.web_thue_gia_su_online.service.tutoring.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorProfileUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.tutoring.TutorProfileMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.tutoring.TutorProfileRepository;
import org.example.web_thue_gia_su_online.service.tutoring.TutorProfileService;
import org.springframework.dao.DataIntegrityViolationException;
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
public class TutorProfileServiceImpl implements TutorProfileService {

    TutorProfileRepository tutorProfileRepository;
    UserRepository userRepository;
    TutorProfileMapper tutorProfileMapper;

    @Override
    @Transactional
    public TutorProfile createTutorProfile(User user, TutorApplication tutorApplication) {

        if (user == null || tutorApplication == null) throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        if (tutorProfileRepository.existsByUserId(user.getId()))
            throw new AppException(ErrorCode.TUTOR_PROFILE_EXISTED);

        Instant now = Instant.now();
        TutorProfile tutorProfile = TutorProfile.builder()
                .user(user)
                .bio(tutorApplication.getBio())
                .subjectsCanTeach(tutorApplication.getSubjectsCanTeach())
                .createdAt(now)
                .updatedAt(now)
                .build();

        try {
            return tutorProfileRepository.save(tutorProfile);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public List<TutorProfileResponse> getTutorProfiles() {
        return tutorProfileRepository.findAll().stream()
                .map(tutorProfileMapper::toTutorProfileResponse)
                .toList();
    }

    @Override
    public TutorProfileResponse getMyTutorProfile() {

        String userId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        TutorProfile tutorProfile = tutorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_PROFILE_NOT_FOUND));

        return tutorProfileMapper.toTutorProfileResponse(tutorProfile);
    }

    @Override
    public TutorProfileResponse getTutorProfile(String userId) {
        TutorProfile tutorProfile = tutorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_PROFILE_NOT_FOUND));

        return tutorProfileMapper.toTutorProfileResponse(tutorProfile);
    }

    @Override
    @Transactional
    public TutorProfileResponse updateMyTutorProfile(TutorProfileUpdateRequest request) {

        String userId = Objects.requireNonNull(
                        SecurityContextHolder.getContext().getAuthentication())
                .getName();

        TutorProfile tutorProfile = tutorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_PROFILE_NOT_FOUND));

        tutorProfileMapper.updateTutorProfileResponse(tutorProfile, request);
        tutorProfile.setUpdatedAt(Instant.now());

        return tutorProfileMapper.toTutorProfileResponse(tutorProfileRepository.save(tutorProfile));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public TutorProfileResponse updateTutorProfile(TutorProfileUpdateRequest request, String userId) {
        TutorProfile tutorProfile = tutorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_PROFILE_NOT_FOUND));

        tutorProfileMapper.updateTutorProfileResponse(tutorProfile, request);
        tutorProfile.setUpdatedAt(Instant.now());

        return tutorProfileMapper.toTutorProfileResponse(tutorProfileRepository.save(tutorProfile));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteTutorProfile(String userId) {
        TutorProfile tutorProfile = tutorProfileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_PROFILE_NOT_FOUND));

        tutorProfileRepository.delete(tutorProfile);
    }
}
