package org.example.web_thue_gia_su_online.service.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.CourseEnrollmentMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.impl.CourseEnrollmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseEnrollmentServiceTest {

    @Mock
    private CourseEnrollmentRepository repository;

    @Mock
    private CourseEnrollmentMapper mapper;

    @InjectMocks
    private CourseEnrollmentServiceImpl service;

    @Test
    void enrollCourse_shouldReturnResponse() {
        CourseEnrollmentCreationRequest request = CourseEnrollmentCreationRequest.builder()
                .studentId("student-1")
                .courseId("course-1")
                .build();

        CourseEnrollment enrollment = CourseEnrollment.builder().id("1").build();
        CourseEnrollmentResponse response =
                CourseEnrollmentResponse.builder().id("1").build();
        when(mapper.toEntity(request)).thenReturn(enrollment);
        when(repository.save(any())).thenReturn(enrollment);
        when(mapper.toResponse(any())).thenReturn(response);

        CourseEnrollmentResponse result = service.enrollCourse(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getEnrollment_shouldReturnResponse() {
        CourseEnrollment enrollment = CourseEnrollment.builder().id("1").build();
        CourseEnrollmentResponse response =
                CourseEnrollmentResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(enrollment));
        when(mapper.toResponse(enrollment)).thenReturn(response);

        CourseEnrollmentResponse result = service.getEnrollment("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getEnrollment_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getEnrollment("1"));
    }

    @Test
    void getEnrollmentsByStudent_shouldReturnList() {
        CourseEnrollment enrollment = CourseEnrollment.builder().id("1").build();
        CourseEnrollmentResponse response =
                CourseEnrollmentResponse.builder().id("1").build();
        when(repository.findByStudentId("student-1")).thenReturn(List.of(enrollment));
        when(mapper.toResponse(enrollment)).thenReturn(response);

        List<CourseEnrollmentResponse> result = service.getEnrollmentsByStudent("student-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getEnrollmentsByCourse_shouldReturnList() {
        CourseEnrollment enrollment = CourseEnrollment.builder().id("1").build();
        CourseEnrollmentResponse response =
                CourseEnrollmentResponse.builder().id("1").build();
        when(repository.findByCourseId("course-1")).thenReturn(List.of(enrollment));
        when(mapper.toResponse(enrollment)).thenReturn(response);

        List<CourseEnrollmentResponse> result = service.getEnrollmentsByCourse("course-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void cancelEnrollment_shouldSucceed() {
        CourseEnrollment enrollment = CourseEnrollment.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(enrollment));

        service.cancelEnrollment("1");

        verify(repository).delete(enrollment);
    }
}
