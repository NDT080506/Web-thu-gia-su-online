package org.example.web_thue_gia_su_online.service.identity_and_administration.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.TutorProfileCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.TutorProfileUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.TutorProfileResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.TutorProfile;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.TutorProfileMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.TutorProfileRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.identity_and_administration.TutorProfileService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
    public TutorProfileResponse createTutorProfile(TutorProfileCreationRequest request, String userId) {
        if (tutorProfileRepository.existsById(userId)) {
            throw new RuntimeException("Tutor profile already exists");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        TutorProfile tutorProfile = tutorProfileMapper.toTutorProfile(request);
        tutorProfile.setUser(user);
        tutorProfile.setCreatedAt(Instant.now());

        try {
            tutorProfileRepository.save(tutorProfile);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }

        return tutorProfileMapper.toTutorProfileResponse(tutorProfile);
    }

    @Override
    public List<TutorProfileResponse> getTutorProfiles() {
        return tutorProfileRepository.findAll().stream()
                .map(tutorProfileMapper::toTutorProfileResponse)
                .toList();
    }

    @Override
    public TutorProfileResponse getTutorProfile(String id) {
        TutorProfile tutorProfile =
                tutorProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Tutor Profile not found"));

        return tutorProfileMapper.toTutorProfileResponse(tutorProfile);
    }

    @Override
    public TutorProfileResponse updateTutorProfile(TutorProfileUpdateRequest request, String id) {
        TutorProfile tutorProfile =
                tutorProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Tutor Profile not found"));

        tutorProfileMapper.updateTutorProfileResponse(tutorProfile, request);

        return tutorProfileMapper.toTutorProfileResponse(tutorProfileRepository.save(tutorProfile));
    }

    @Override
    public void deleteTutorProfile(String id) {
        TutorProfile tutorProfile =
                tutorProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Tutor Profile not found"));

        tutorProfileRepository.delete(tutorProfile);
    }
}
