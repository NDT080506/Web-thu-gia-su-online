package org.example.web_thue_gia_su_online.controller.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @Test
    void create_shouldReturnResponse() {
        CourseCreationRequest request = CourseCreationRequest.builder()
                .tutorId("tutor-1")
                .title("Math 101")
                .price(50000L)
                .build();
        CourseResponse response = CourseResponse.builder().id("1").build();
        when(courseService.create(eq(request))).thenReturn(response);
        ApiResponse<CourseResponse> apiResponse = courseController.create(request);
        assertNotNull(apiResponse);
        verify(courseService).create(eq(request));
    }

    @Test
    void getById_shouldReturnResponse() {
        CourseResponse response = CourseResponse.builder().id("1").build();
        when(courseService.getById("1")).thenReturn(response);
        ApiResponse<CourseResponse> apiResponse = courseController.getById("1");
        assertNotNull(apiResponse);
        verify(courseService).getById("1");
    }

    @Test
    void getByTutorId_shouldReturnList() {
        List<CourseResponse> list = List.of(CourseResponse.builder().id("1").build());
        when(courseService.getByTutorId("tutor-1")).thenReturn(list);
        ApiResponse<List<CourseResponse>> apiResponse = courseController.getByTutorId("tutor-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }
}
