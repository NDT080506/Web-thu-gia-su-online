package org.example.web_thue_gia_su_online.service.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.LessonCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.LessonResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Lesson;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.LessonMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.LessonRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.impl.LessonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository repository;

    @Mock
    private LessonMapper mapper;

    @InjectMocks
    private LessonServiceImpl service;

    @Test
    void createLesson_shouldReturnResponse() {
        LessonCreationRequest request = LessonCreationRequest.builder()
                .courseId("course-1")
                .title("Lesson 1")
                .build();

        Lesson lesson = Lesson.builder().id("1").build();
        LessonResponse response = LessonResponse.builder().id("1").build();
        when(mapper.toEntity(request)).thenReturn(lesson);
        when(repository.save(any())).thenReturn(lesson);
        when(mapper.toResponse(any())).thenReturn(response);

        LessonResponse result = service.createLesson(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getLesson_shouldReturnResponse() {
        Lesson lesson = Lesson.builder().id("1").build();
        LessonResponse response = LessonResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(lesson));
        when(mapper.toResponse(lesson)).thenReturn(response);

        LessonResponse result = service.getLesson("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getLesson_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getLesson("1"));
    }

    @Test
    void getLessonsByCourseId_shouldReturnList() {
        Lesson lesson = Lesson.builder().id("1").build();
        LessonResponse response = LessonResponse.builder().id("1").build();
        when(repository.findByCourseId("course-1")).thenReturn(List.of(lesson));
        when(mapper.toResponse(lesson)).thenReturn(response);

        List<LessonResponse> result = service.getLessonsByCourseId("course-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateLesson_shouldReturnResponse() {
        Lesson lesson = Lesson.builder().id("1").build();
        LessonResponse response = LessonResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(lesson));
        when(repository.save(any())).thenReturn(lesson);
        when(mapper.toResponse(any())).thenReturn(response);

        LessonResponse result = service.updateLesson("1", any());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void deleteLesson_shouldSucceed() {
        Lesson lesson = Lesson.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(lesson));

        service.deleteLesson("1");

        verify(repository).delete(lesson);
    }
}
