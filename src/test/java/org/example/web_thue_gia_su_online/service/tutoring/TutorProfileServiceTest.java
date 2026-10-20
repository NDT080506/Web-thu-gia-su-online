package org.example.web_thue_gia_su_online.service.tutoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.example.web_thue_gia_su_online.mapper.tutoring.TutorProfileMapper;
import org.example.web_thue_gia_su_online.repository.tutoring.TutorProfileRepository;
import org.example.web_thue_gia_su_online.service.tutoring.impl.TutorProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TutorProfileServiceTest {

    @Mock
    private TutorProfileRepository repository;

    @Mock
    private TutorProfileMapper mapper;

    @InjectMocks
    private TutorProfileServiceImpl service;

    @Test
    void getTutorProfile_shouldReturnResponse() {
        TutorProfile profile = TutorProfile.builder().userId("1").build();
        TutorProfileResponse response =
                TutorProfileResponse.builder().userId("1").build();
        when(repository.findByUserId("1")).thenReturn(Optional.of(profile));
        when(mapper.toTutorProfileResponse(any())).thenReturn(response);

        TutorProfileResponse result = service.getTutorProfile("1");

        assertNotNull(result);
        assertEquals("1", result.getUserId());
    }

    @Test
    void getTutorProfile_notFound_shouldThrow() {
        when(repository.findByUserId("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getTutorProfile("1"));
    }

    @Test
    void getTutorProfiles_shouldReturnList() {
        TutorProfile profile = TutorProfile.builder().userId("1").build();
        TutorProfileResponse response =
                TutorProfileResponse.builder().userId("1").build();
        when(repository.findAll()).thenReturn(List.of(profile));
        when(mapper.toTutorProfileResponse(any())).thenReturn(response);

        List<TutorProfileResponse> result = service.getTutorProfiles();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateTutorProfile_shouldReturnResponse() {
        TutorProfile profile = TutorProfile.builder().userId("1").build();
        TutorProfileResponse response =
                TutorProfileResponse.builder().userId("1").build();
        when(repository.findByUserId("1")).thenReturn(Optional.of(profile));
        when(repository.save(any())).thenReturn(profile);
        when(mapper.toTutorProfileResponse(any())).thenReturn(response);

        TutorProfileResponse result = service.updateTutorProfile(any(), "1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void deleteTutorProfile_shouldSucceed() {
        TutorProfile profile = TutorProfile.builder().userId("1").build();
        when(repository.findByUserId("1")).thenReturn(Optional.of(profile));
        doNothing().when(repository).delete(profile);

        service.deleteTutorProfile("1");

        verify(repository).delete(profile);
    }
}
