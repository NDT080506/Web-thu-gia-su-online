package org.example.web_thue_gia_su_online.controller.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.LessonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController controller;

    @Test
    void createLesson_shouldReturnResponse() {
        LessonCreationRequest request = LessonCreationRequest.builder()
                .courseId("course-1")
                .title("Lesson 1")
                .build();
        LessonResponse response = LessonResponse.builder().id("1").build();
        when(lessonService.createLesson(eq(request))).thenReturn(response);
        ApiResponse<LessonResponse> apiResponse = controller.createLesson(request);
        assertNotNull(apiResponse);
        verify(lessonService).createLesson(eq(request));
    }

    @Test
    void getLesson_shouldReturnResponse() {
        LessonResponse response = LessonResponse.builder().id("1").build();
        when(lessonService.getLesson("1")).thenReturn(response);
        ApiResponse<LessonResponse> apiResponse = controller.getLesson("1");
        assertNotNull(apiResponse);
        verify(lessonService).getLesson("1");
    }

    @Test
    void getLessonsByCourseId_shouldReturnList() {
        List<LessonResponse> list = List.of(LessonResponse.builder().id("1").build());
        when(lessonService.getLessonsByCourseId("course-1")).thenReturn(list);
        ApiResponse<List<LessonResponse>> apiResponse = controller.getLessonsByCourseId("course-1");
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void updateLesson_shouldReturnResponse() {
        LessonUpdateRequest request =
                LessonUpdateRequest.builder().title("Updated Lesson").build();
        LessonResponse response = LessonResponse.builder().id("1").build();
        when(lessonService.updateLesson(eq("1"), eq(request))).thenReturn(response);
        ApiResponse<LessonResponse> apiResponse = controller.updateLesson("1", request);
        assertNotNull(apiResponse);
        verify(lessonService).updateLesson(eq("1"), eq(request));
    }

    @Test
    void deleteLesson_shouldReturnSuccessMessage() {
        doNothing().when(lessonService).deleteLesson("1");
        ApiResponse<Void> apiResponse = controller.deleteLesson("1");
        assertNotNull(apiResponse);
        assertEquals("Lesson deleted successfully", apiResponse.getMessage());
    }
}
