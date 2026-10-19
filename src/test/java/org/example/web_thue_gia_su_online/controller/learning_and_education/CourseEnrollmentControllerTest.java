package org.example.web_thue_gia_su_online.controller.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseEnrollmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseEnrollmentControllerTest {

    @Mock
    private CourseEnrollmentService service;

    @InjectMocks
    private CourseEnrollmentController controller;

    @Test
    void enrollCourse_shouldReturnResponse() {
        CourseEnrollmentCreationRequest request = CourseEnrollmentCreationRequest.builder()
                .studentId("student-1")
                .courseId("course-1")
                .pricePaid(50000L)
                .build();
        CourseEnrollmentResponse response =
                CourseEnrollmentResponse.builder().id("1").build();
        when(service.enrollCourse(eq(request))).thenReturn(response);
        ApiResponse<CourseEnrollmentResponse> apiResponse = controller.enrollCourse(request);
        assertNotNull(apiResponse);
        verify(service).enrollCourse(eq(request));
    }

    @Test
    void getEnrollment_shouldReturnResponse() {
        CourseEnrollmentResponse response =
                CourseEnrollmentResponse.builder().id("1").build();
        when(service.getEnrollment("1")).thenReturn(response);
        ApiResponse<CourseEnrollmentResponse> apiResponse = controller.getEnrollment("1");
        assertNotNull(apiResponse);
        verify(service).getEnrollment("1");
    }

    @Test
    void getEnrollmentsByStudent_shouldReturnList() {
        List<CourseEnrollmentResponse> list =
                List.of(CourseEnrollmentResponse.builder().id("1").build());
        when(service.getEnrollmentsByStudent("student-1")).thenReturn(list);
        ApiResponse<List<CourseEnrollmentResponse>> apiResponse = controller.getEnrollmentsByStudent("student-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getEnrollmentsByCourse_shouldReturnList() {
        List<CourseEnrollmentResponse> list =
                List.of(CourseEnrollmentResponse.builder().id("1").build());
        when(service.getEnrollmentsByCourse("course-1")).thenReturn(list);
        ApiResponse<List<CourseEnrollmentResponse>> apiResponse = controller.getEnrollmentsByCourse("course-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void updateEnrollment_shouldReturnResponse() {
        CourseEnrollmentUpdateRequest request =
                CourseEnrollmentUpdateRequest.builder().progressPercent(50).build();
        CourseEnrollmentResponse response =
                CourseEnrollmentResponse.builder().id("1").build();
        when(service.updateEnrollment(eq("1"), eq(request))).thenReturn(response);
        ApiResponse<CourseEnrollmentResponse> apiResponse = controller.updateEnrollment("1", request);
        assertNotNull(apiResponse);
        verify(service).updateEnrollment(eq("1"), eq(request));
    }

    @Test
    void cancelEnrollment_shouldReturnSuccessMessage() {
        doNothing().when(service).cancelEnrollment("1");
        ApiResponse<Void> apiResponse = controller.cancelEnrollment("1");
        assertNotNull(apiResponse);
        assertEquals("Enrollment cancelled successfully", apiResponse.getMessage());
    }
}
