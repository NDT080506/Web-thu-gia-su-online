package org.example.web_thue_gia_su_online.service.tutoring.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.tutoring.TutorApplicationMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.tutoring.TutorApplicationRepository;
import org.example.web_thue_gia_su_online.service.tutoring.TutorApplicationService;
import org.springframework.stereotype.Service;

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

    @Override
    public TutorApplicationResponse createTutorApplication(TutorApplicationCreationRequest request, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        TutorApplication tutorApplication = tutorApplicationMapper.toTutorApplication(request);
        tutorApplication.setUser(user);
        tutorApplication.setCreatedAt(Instant.now());

        tutorApplicationRepository.save(tutorApplication);

        return tutorApplicationMapper.toTutorApplicationResponse(tutorApplication);
    }

    @Override
    public TutorApplicationResponse getTutorApplication(String id) {

        TutorApplication tutorApplication = tutorApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_APPLICATION_NOT_FOUND));

        return tutorApplicationMapper.toTutorApplicationResponse(tutorApplication);
    }

    @Override
    public List<TutorApplicationResponse> getTutorApplications() {
        return tutorApplicationRepository.findAll().stream()
                .map(tutorApplicationMapper::toTutorApplicationResponse)
                .toList();
    }

    @Override
    public void deleteTutorApplication(String id) {

        TutorApplication tutorApplication = tutorApplicationRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TUTOR_APPLICATION_NOT_FOUND));

        tutorApplicationRepository.delete(tutorApplication);
    }
}
