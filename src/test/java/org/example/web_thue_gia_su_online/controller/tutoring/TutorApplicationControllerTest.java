package org.example.web_thue_gia_su_online.controller.tutoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.tutoring.TutorApplicationCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.tutoring.TutorApplicationResponse;
import org.example.web_thue_gia_su_online.service.tutoring.TutorApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TutorApplicationControllerTest {

    @Mock
    private TutorApplicationService service;

    @InjectMocks
    private TutorApplicationController controller;

    @Test
    void createTutorApplication_shouldReturnResponse() {
        TutorApplicationCreationRequest request = TutorApplicationCreationRequest.builder()
                .bio("Experienced tutor")
                .subjectsCanTeach("Math, Physics")
                .cvUrl("http://cv.com")
                .testSubmission("http://test.com")
                .build();
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(service.createTutorApplication(eq(request))).thenReturn(response);
        ApiResponse<TutorApplicationResponse> apiResponse = controller.createTutorApplication(request);
        assertNotNull(apiResponse);
        verify(service).createTutorApplication(eq(request));
    }

    @Test
    void getMyTutorApplication_shouldReturnResponse() {
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(service.getMyTutorApplication()).thenReturn(response);
        ApiResponse<TutorApplicationResponse> apiResponse = controller.getMyTutorApplication();
        assertNotNull(apiResponse);
        verify(service).getMyTutorApplication();
    }

    @Test
    void deleteMyTutorApplication_shouldReturnSuccessMessage() {
        doNothing().when(service).deleteMyTutorApplication();
        ApiResponse<Void> apiResponse = controller.deleteMyTutorApplication();
        assertNotNull(apiResponse);
        assertEquals("Tutor application has been deleted", apiResponse.getMessage());
    }

    @Test
    void getTutorApplications_shouldReturnList() {
        List<TutorApplicationResponse> list =
                List.of(TutorApplicationResponse.builder().id("1").build());
        when(service.getTutorApplications()).thenReturn(list);
        ApiResponse<List<TutorApplicationResponse>> apiResponse = controller.getTutorApplications();
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getTutorApplication_shouldReturnResponse() {
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(service.getTutorApplication("1")).thenReturn(response);
        ApiResponse<TutorApplicationResponse> apiResponse = controller.getTutorApplication("1");
        assertNotNull(apiResponse);
        verify(service).getTutorApplication("1");
    }

    @Test
    void approveTutorApplication_shouldReturnResponse() {
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(service.approveTutorApplication("1")).thenReturn(response);
        ApiResponse<TutorApplicationResponse> apiResponse = controller.approveTutorApplication("1");
        assertNotNull(apiResponse);
        verify(service).approveTutorApplication("1");
    }

    @Test
    void rejectTutorApplication_shouldReturnResponse() {
        TutorApplicationResponse response =
                TutorApplicationResponse.builder().id("1").build();
        when(service.rejectTutorApplication("1")).thenReturn(response);
        ApiResponse<TutorApplicationResponse> apiResponse = controller.rejectTutorApplication("1");
        assertNotNull(apiResponse);
        verify(service).rejectTutorApplication("1");
    }

    @Test
    void deleteTutorApplication_shouldReturnSuccessMessage() {
        doNothing().when(service).deleteTutorApplication("1");
        ApiResponse<Void> apiResponse = controller.deleteTutorApplication("1");
        assertNotNull(apiResponse);
        assertEquals("Tutor application has been deleted", apiResponse.getMessage());
    }
}
