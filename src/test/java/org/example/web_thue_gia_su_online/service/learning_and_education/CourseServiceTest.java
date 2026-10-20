package org.example.web_thue_gia_su_online.service.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.CourseMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository repository;

    @Mock
    private CourseMapper mapper;

    @InjectMocks
    private CourseServiceImpl service;

    @Test
    void create_shouldReturnResponse() {
        CourseCreationRequest request = CourseCreationRequest.builder()
                .tutorId("tutor-1")
                .title("Math 101")
                .price(100000L)
                .build();

        Course course = Course.builder().id("1").build();
        CourseResponse response = CourseResponse.builder().id("1").build();
        when(mapper.toEntity(request)).thenReturn(course);
        when(repository.save(any())).thenReturn(course);
        when(mapper.toResponse(any())).thenReturn(response);

        CourseResponse result = service.create(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getById_shouldReturnResponse() {
        Course course = Course.builder().id("1").build();
        CourseResponse response = CourseResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(course));
        when(mapper.toResponse(course)).thenReturn(response);

        CourseResponse result = service.getById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getById_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById("1"));
    }

    @Test
    void getByTutorId_shouldReturnList() {
        Course course = Course.builder().id("1").build();
        CourseResponse response = CourseResponse.builder().id("1").build();
        when(repository.findByTutorId("tutor-1")).thenReturn(List.of(course));
        when(mapper.toResponse(course)).thenReturn(response);

        List<CourseResponse> result = service.getByTutorId("tutor-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
