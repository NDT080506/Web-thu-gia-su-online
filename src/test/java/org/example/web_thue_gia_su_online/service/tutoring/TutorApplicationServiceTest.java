package org.example.web_thue_gia_su_online.service.tutoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.enums.RequestStatus;
import org.example.web_thue_gia_su_online.mapper.tutoring.TutorApplicationMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.tutoring.TutorApplicationRepository;
import org.example.web_thue_gia_su_online.service.tutoring.impl.TutorApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class TutorApplicationServiceTest {

    @Mock
    private TutorApplicationRepository repository;

    @Mock
    private TutorApplicationMapper mapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TutorProfileService tutorProfileService;

    @InjectMocks
    private TutorApplicationServiceImpl service;

    @Test
    void createTutorApplication_shouldReturnResponse() {
        TutorApplicationCreationRequest request = TutorApplicationCreationRequest.builder()
                .bio("Test bio")
                .subjectsCanTeach("Math, Physics")
                .cvUrl("https://example.com/cv.pdf")
                .testSubmission("https://example.com/test")
                .build();

        TutorApplication app = TutorApplication.builder().id("1").build();
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        User user = User.builder().id("user1").build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user1");

        when(repository.existsByUserIdAndStatus(eq("user1"), eq(RequestStatus.PENDING)))
                .thenReturn(false);
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(mapper.toTutorApplication(eq(request))).thenReturn(app);
        when(repository.save(any())).thenReturn(app);
        when(mapper.toTutorApplicationResponse(any())).thenReturn(response);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            TutorApplicationResponse result = service.createTutorApplication(request);

            assertNotNull(result);
            verify(repository).save(any());
        }
    }

    @Test
    void getTutorApplication_shouldReturnResponse() {
        TutorApplication app = TutorApplication.builder().id("1").build();
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(app));
        when(mapper.toTutorApplicationResponse(any())).thenReturn(response);

        TutorApplicationResponse result = service.getTutorApplication("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getTutorApplication_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getTutorApplication("1"));
    }

    @Test
    void getTutorApplications_shouldReturnList() {
        TutorApplication app = TutorApplication.builder().id("1").build();
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(repository.findAll()).thenReturn(List.of(app));
        when(mapper.toTutorApplicationResponse(any())).thenReturn(response);

        List<TutorApplicationResponse> result = service.getTutorApplications();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void approveTutorApplication_shouldReturnResponse() {
        User user = User.builder().id("user1").build();
        TutorApplication app = TutorApplication.builder().id("1").user(user).build();
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(app));
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(repository.save(any())).thenReturn(app);
        when(mapper.toTutorApplicationResponse(any())).thenReturn(response);

        TutorApplicationResponse result = service.approveTutorApplication("1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void rejectTutorApplication_shouldReturnResponse() {
        TutorApplication app = TutorApplication.builder().id("1").build();
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(app));
        when(repository.save(any())).thenReturn(app);
        when(mapper.toTutorApplicationResponse(any())).thenReturn(response);

        TutorApplicationResponse result = service.rejectTutorApplication("1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void deleteTutorApplication_shouldSucceed() {
        TutorApplication app = TutorApplication.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(app));
        doNothing().when(repository).delete(app);

        service.deleteTutorApplication("1");

        verify(repository).delete(app);
    }
}
