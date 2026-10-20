package org.example.web_thue_gia_su_online.controller.tutoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorProfileUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorProfileResponse;
import org.example.web_thue_gia_su_online.service.tutoring.TutorProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TutorProfileControllerTest {

    @Mock
    private TutorProfileService service;

    @InjectMocks
    private TutorProfileController controller;

    @Test
    void getMyTutorProfile_shouldReturnResponse() {
        TutorProfileResponse response =
                TutorProfileResponse.builder().userId("1").build();
        when(service.getMyTutorProfile()).thenReturn(response);
        ApiResponse<TutorProfileResponse> apiResponse = controller.getMyTutorProfile();
        assertNotNull(apiResponse);
        verify(service).getMyTutorProfile();
    }

    @Test
    void getTutorProfiles_shouldReturnList() {
        List<TutorProfileResponse> list =
                List.of(TutorProfileResponse.builder().userId("1").build());
        when(service.getTutorProfiles()).thenReturn(list);
        ApiResponse<List<TutorProfileResponse>> apiResponse = controller.getTutorProfiles();
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getTutorProfile_shouldReturnResponse() {
        TutorProfileResponse response =
                TutorProfileResponse.builder().userId("1").build();
        when(service.getTutorProfile("1")).thenReturn(response);
        ApiResponse<TutorProfileResponse> apiResponse = controller.getTutorProfile("1");
        assertNotNull(apiResponse);
        verify(service).getTutorProfile("1");
    }

    @Test
    void updateMyTutorProfile_shouldReturnResponse() {
        TutorProfileUpdateRequest request =
                TutorProfileUpdateRequest.builder().bio("Updated bio").build();
        TutorProfileResponse response =
                TutorProfileResponse.builder().userId("1").build();
        when(service.updateMyTutorProfile(eq(request))).thenReturn(response);
        ApiResponse<TutorProfileResponse> apiResponse = controller.updateMyTutorProfile(request);
        assertNotNull(apiResponse);
        verify(service).updateMyTutorProfile(eq(request));
    }

    @Test
    void updateTutorProfile_shouldReturnResponse() {
        TutorProfileUpdateRequest request =
                TutorProfileUpdateRequest.builder().bio("Admin updated bio").build();
        TutorProfileResponse response =
                TutorProfileResponse.builder().userId("1").build();
        when(service.updateTutorProfile(eq(request), eq("1"))).thenReturn(response);
        ApiResponse<TutorProfileResponse> apiResponse = controller.updateTutorProfile(request, "1");
        assertNotNull(apiResponse);
        verify(service).updateTutorProfile(eq(request), eq("1"));
    }

    @Test
    void deleteTutorProfile_shouldReturnSuccessMessage() {
        doNothing().when(service).deleteTutorProfile("1");
        ApiResponse<Void> apiResponse = controller.deleteTutorProfile("1");
        assertNotNull(apiResponse);
        assertEquals("Tutor profile has been deleted", apiResponse.getMessage());
    }
}
