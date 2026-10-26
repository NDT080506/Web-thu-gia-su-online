package org.example.web_thue_gia_su_online.service.learning_and_education.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.CourseEnrollmentCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.CourseEnrollmentResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Course;
import org.example.web_thue_gia_su_online.entity.learning_and_education.CourseEnrollment;
import org.example.web_thue_gia_su_online.enums.EnrollmentStatus;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.CourseEnrollmentMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseEnrollmentRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class CourseEnrollmentServiceImplTest {

    @Mock
    CourseEnrollmentRepository courseEnrollmentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseEnrollmentMapper courseEnrollmentMapper;

    @InjectMocks
    CourseEnrollmentServiceImpl courseEnrollmentService;

    private String studentId;
    private String tutorId;
    private String courseId;

    @BeforeEach
    void setUp() {
        studentId = "student-uuid-123";
        tutorId = "tutor-uuid-456";
        courseId = "course-uuid-789";
    }

    private void setAuthenticatedUser(String userId) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(userId);
        SecurityContextHolder.setContext(new SecurityContext() {
            @Override
            public Authentication getAuthentication() {
                return authentication;
            }

            @Override
            public void setAuthentication(Authentication authentication) {}
        });
    }

    private User createStudent() {
        return User.builder().id(studentId).build();
    }

    private User createTutor() {
        return User.builder().id(tutorId).build();
    }

    private Course createActiveCourse() {
        return Course.builder()
                .id(courseId)
                .tutor(createTutor())
                .title("Math 101")
                .price(150000)
                .platformFeePercent(10)
                .active(true)
                .build();
    }

    private CourseEnrollment createEnrollment() {
        return CourseEnrollment.builder()
                .id("enrollment-uuid-111")
                .student(createStudent())
                .course(createActiveCourse())
                .totalPrice(150000)
                .platformFee(15000)
                .tutorIncome(135000)
                .status(EnrollmentStatus.PENDING)
                .build();
    }

    @Test
    void enrollCourse_success_whenValid() {
        setAuthenticatedUser(studentId);

        CourseEnrollmentCreationRequest request = new CourseEnrollmentCreationRequest();
        request.setCourseId(courseId);

        Course course = createActiveCourse();
        User student = createStudent();
        CourseEnrollment savedEnrollment = createEnrollment();

        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseEnrollmentRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.empty());
        when(courseEnrollmentRepository.save(any(CourseEnrollment.class))).thenReturn(savedEnrollment);
        when(courseEnrollmentMapper.toResponse(savedEnrollment)).thenReturn(new CourseEnrollmentResponse());

        CourseEnrollmentResponse response = courseEnrollmentService.enrollCourse(request);

        assertThat(response).isNotNull();
        verify(courseEnrollmentRepository).save(any(CourseEnrollment.class));
    }

    @Test
    void enrollCourse_fails_whenAlreadyEnrolled() {
        setAuthenticatedUser(studentId);

        CourseEnrollmentCreationRequest request = new CourseEnrollmentCreationRequest();
        request.setCourseId(courseId);

        when(userRepository.findById(studentId)).thenReturn(Optional.of(createStudent()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(createActiveCourse()));
        when(courseEnrollmentRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.of(createEnrollment()));

        assertThatThrownBy(() -> courseEnrollmentService.enrollCourse(request)).isInstanceOf(AppException.class);

        verify(courseEnrollmentRepository, never()).save(any());
    }

    @Test
    void enrollCourse_fails_whenCourseNotFound() {
        setAuthenticatedUser(studentId);

        CourseEnrollmentCreationRequest request = new CourseEnrollmentCreationRequest();
        request.setCourseId("non-existent");

        when(userRepository.findById(studentId)).thenReturn(Optional.of(createStudent()));
        when(courseRepository.findById("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseEnrollmentService.enrollCourse(request)).isInstanceOf(AppException.class);

        verify(courseEnrollmentRepository, never()).save(any());
    }

    @Test
    void enrollCourse_fails_whenCourseInactive() {
        setAuthenticatedUser(studentId);

        CourseEnrollmentCreationRequest request = new CourseEnrollmentCreationRequest();
        request.setCourseId(courseId);

        Course inactiveCourse =
                Course.builder().id(courseId).tutor(createTutor()).active(false).build();

        when(userRepository.findById(studentId)).thenReturn(Optional.of(createStudent()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(inactiveCourse));

        assertThatThrownBy(() -> courseEnrollmentService.enrollCourse(request)).isInstanceOf(AppException.class);

        verify(courseEnrollmentRepository, never()).save(any());
    }

    @Test
    void enrollCourse_usesCoursePrice_notClientPrice() {
        setAuthenticatedUser(studentId);

        CourseEnrollmentCreationRequest request = new CourseEnrollmentCreationRequest();
        request.setCourseId(courseId);

        Course course = Course.builder()
                .id(courseId)
                .tutor(createTutor())
                .price(300000)
                .platformFeePercent(20)
                .active(true)
                .build();

        CourseEnrollment savedEnrollment = CourseEnrollment.builder()
                .id("enrollment-uuid-222")
                .totalPrice(300000)
                .platformFee(60000)
                .tutorIncome(240000)
                .build();

        when(userRepository.findById(studentId)).thenReturn(Optional.of(createStudent()));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseEnrollmentRepository.findByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(Optional.empty());
        when(courseEnrollmentRepository.save(any(CourseEnrollment.class))).thenReturn(savedEnrollment);
        when(courseEnrollmentMapper.toResponse(savedEnrollment)).thenReturn(new CourseEnrollmentResponse());

        courseEnrollmentService.enrollCourse(request);

        verify(courseEnrollmentRepository)
                .save(argThat(enrollment -> enrollment.getTotalPrice() == 300000
                        && enrollment.getPlatformFee() == 60000
                        && enrollment.getTutorIncome() == 240000));
    }

    @Test
    void updateEnrollment_cancelSuccess_whenStudentOwns() {
        setAuthenticatedUser(studentId);

        CourseEnrollment enrollment = createEnrollment();
        when(courseEnrollmentRepository.findById("enrollment-1")).thenReturn(Optional.of(enrollment));
        when(courseEnrollmentRepository.save(any(CourseEnrollment.class))).thenReturn(enrollment);
        when(courseEnrollmentMapper.toResponse(enrollment)).thenReturn(new CourseEnrollmentResponse());

        CourseEnrollmentResponse response = courseEnrollmentService.updateEnrollment("enrollment-1");

        assertThat(response).isNotNull();
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
    }

    @Test
    void updateEnrollment_cancelFails_whenNotOwner() {
        setAuthenticatedUser("other-user-id");

        CourseEnrollment enrollment = createEnrollment();
        when(courseEnrollmentRepository.findById("enrollment-1")).thenReturn(Optional.of(enrollment));

        assertThatThrownBy(() -> courseEnrollmentService.updateEnrollment("enrollment-1"))
                .isInstanceOf(AccessDeniedException.class);

        verify(courseEnrollmentRepository, never()).save(any());
    }

    @Test
    void getEnrollmentsByCourse_fails_whenTutorNotOwner() {
        setAuthenticatedUser("other-tutor-id");

        Course otherCourse = Course.builder()
                .id(courseId)
                .tutor(User.builder().id("real-tutor-id").build())
                .build();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(otherCourse));

        assertThatThrownBy(() -> courseEnrollmentService.getEnrollmentsByCourse(courseId))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void getEnrollmentsByCourse_success_whenTutorIsOwner() {
        setAuthenticatedUser(tutorId);

        Course course = createActiveCourse();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseEnrollmentRepository.findByCourseId(courseId)).thenReturn(Collections.emptyList());

        var result = courseEnrollmentService.getEnrollmentsByCourse(courseId);
        assertThat(result).isEmpty();
    }
}
