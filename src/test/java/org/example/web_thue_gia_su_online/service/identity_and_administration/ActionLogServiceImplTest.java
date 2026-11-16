package org.example.web_thue_gia_su_online.service.identity_and_administration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.ActionLog;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.ActionCode;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.UserRole;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.ActionLogMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.ActionLogRepository;
import org.example.web_thue_gia_su_online.service.identity_and_administration.impl.ActionLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActionLogServiceImpl Unit Tests")
class ActionLogServiceImplTest {

    @Mock
    ActionLogRepository actionLogRepository;

    @Mock
    ActionLogMapper actionLogMapper;

    @InjectMocks
    ActionLogServiceImpl actionLogService;

    private User testUser;
    private ActionLog testActionLog;
    private ActionLogResponse actionLogResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user-123")
                .displayName("Test User")
                .email("test@example.com")
                .role(UserRole.STUDENT)
                .build();

        testActionLog = ActionLog.builder()
                .id("log-123")
                .user(testUser)
                .action(ActionCode.CREATE_USER)
                .targetType("User")
                .targetId("user-123")
                .ipAddress("127.0.0.1")
                .userAgent("Mozilla/5.0")
                .createdAt(Instant.now())
                .build();

        actionLogResponse = ActionLogResponse.builder()
                .id("log-123")
                .userId("user-123")
                .userDisplayName("Test User")
                .action(ActionCode.CREATE_USER)
                .targetType("User")
                .targetId("user-123")
                .ipAddress("127.0.0.1")
                .userAgent("Mozilla/5.0")
                .createdAt(testActionLog.getCreatedAt())
                .build();
    }

    @Nested
    @DisplayName("log tests")
    class LogTests {

        @Test
        @DisplayName("Nên lưu action log thành công khi có HTTP request context")
        void shouldLogActionSuccessfully() {
            // Arrange
            HttpServletRequest mockRequest = mock(HttpServletRequest.class);
            when(mockRequest.getRemoteAddr()).thenReturn("192.168.1.1");
            when(mockRequest.getHeader("User-Agent")).thenReturn("TestAgent/1.0");

            ServletRequestAttributes servletAttributes = new ServletRequestAttributes(mockRequest);
            RequestContextHolder.setRequestAttributes(servletAttributes);

            try {
                when(actionLogRepository.save(any(ActionLog.class))).thenReturn(testActionLog);

                // Act
                actionLogService.log(ActionCode.CREATE_USER, testUser, "User", "user-123");

                // Assert
                ArgumentCaptor<ActionLog> captor = ArgumentCaptor.forClass(ActionLog.class);
                verify(actionLogRepository).save(captor.capture());

                ActionLog captured = captor.getValue();
                assertEquals(ActionCode.CREATE_USER, captured.getAction());
                assertEquals(testUser, captured.getUser());
                assertEquals("User", captured.getTargetType());
                assertEquals("user-123", captured.getTargetId());
                assertEquals("192.168.1.1", captured.getIpAddress());
                assertEquals("TestAgent/1.0", captured.getUserAgent());
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }

        @Test
        @DisplayName("Nên lưu action log với null IP và User-Agent khi không có request context")
        void shouldLogActionWithoutRequestContext() {
            // Arrange - ensure no request context
            RequestContextHolder.resetRequestAttributes();

            when(actionLogRepository.save(any(ActionLog.class))).thenReturn(testActionLog);

            // Act
            actionLogService.log(ActionCode.CREATE_USER, testUser, "User", "user-123");

            // Assert
            ArgumentCaptor<ActionLog> captor = ArgumentCaptor.forClass(ActionLog.class);
            verify(actionLogRepository).save(captor.capture());

            ActionLog captured = captor.getValue();
            assertEquals(ActionCode.CREATE_USER, captured.getAction());
            assertNull(captured.getIpAddress());
            assertNull(captured.getUserAgent());
        }

        @Test
        @DisplayName("Nên lưu action log với các ActionCode khác nhau")
        void shouldLogDifferentActionCode() {
            // Arrange
            when(actionLogRepository.save(any(ActionLog.class))).thenReturn(testActionLog);

            // Act
            actionLogService.log(ActionCode.UPDATE_USER, testUser, "User", "user-123");

            // Assert
            ArgumentCaptor<ActionLog> captor = ArgumentCaptor.forClass(ActionLog.class);
            verify(actionLogRepository).save(captor.capture());
            assertEquals(ActionCode.UPDATE_USER, captor.getValue().getAction());
        }
    }

    @Nested
    @DisplayName("getActionLogs tests")
    class GetActionLogsTests {

        @Test
        @DisplayName("Nên trả về tất cả action logs khi không có bộ lọc")
        void shouldGetAllActionLogsWhenNoFilters() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<ActionLogResponse> expectedPage = new PageImpl<>(java.util.List.of(actionLogResponse));

            when(actionLogRepository.findResponsesAll(pageable)).thenReturn(expectedPage);

            // Act
            Page<ActionLogResponse> result = actionLogService.getActionLogs(null, null, null, null, null, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("log-123", result.getContent().getFirst().getId());
            verify(actionLogRepository).findResponsesAll(pageable);
            verify(actionLogRepository, never()).searchActionLogsResponses(any(), any(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("Nên tìm kiếm theo userId")
        void shouldSearchByUserId() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<ActionLogResponse> expectedPage = new PageImpl<>(java.util.List.of(actionLogResponse));

            when(actionLogRepository.searchActionLogsResponses(
                            eq("user-123"), isNull(), isNull(), isNull(), isNull(), eq(pageable)))
                    .thenReturn(expectedPage);

            // Act
            Page<ActionLogResponse> result =
                    actionLogService.getActionLogs("user-123", null, null, null, null, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(actionLogRepository)
                    .searchActionLogsResponses(eq("user-123"), isNull(), isNull(), isNull(), isNull(), eq(pageable));
        }

        @Test
        @DisplayName("Nên tìm kiếm theo action code")
        void shouldSearchByActionCode() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<ActionLogResponse> expectedPage = new PageImpl<>(java.util.List.of(actionLogResponse));

            when(actionLogRepository.searchActionLogsResponses(
                            isNull(), eq("CREATE_USER"), isNull(), isNull(), isNull(), eq(pageable)))
                    .thenReturn(expectedPage);

            // Act
            Page<ActionLogResponse> result =
                    actionLogService.getActionLogs(null, "CREATE_USER", null, null, null, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @Test
        @DisplayName("Nên tìm kiếm theo khoảng thời gian")
        void shouldSearchByDateRange() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Instant fromDate = Instant.parse("2024-01-01T00:00:00Z");
            Instant toDate = Instant.parse("2024-12-31T23:59:59Z");
            Page<ActionLogResponse> expectedPage = new PageImpl<>(java.util.List.of(actionLogResponse));

            when(actionLogRepository.searchActionLogsResponses(
                            isNull(), isNull(), isNull(), eq(fromDate), eq(toDate), eq(pageable)))
                    .thenReturn(expectedPage);

            // Act
            Page<ActionLogResponse> result =
                    actionLogService.getActionLogs(null, null, null, fromDate, toDate, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @Test
        @DisplayName("Nên tìm kiếm với nhiều bộ lọc kết hợp")
        void shouldSearchWithMultipleFilters() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Instant fromDate = Instant.parse("2024-01-01T00:00:00Z");
            Instant toDate = Instant.parse("2024-12-31T23:59:59Z");
            Page<ActionLogResponse> expectedPage = new PageImpl<>(java.util.List.of(actionLogResponse));

            when(actionLogRepository.searchActionLogsResponses(
                            eq("user-123"), eq("CREATE_USER"), eq("User"), eq(fromDate), eq(toDate), eq(pageable)))
                    .thenReturn(expectedPage);

            // Act
            Page<ActionLogResponse> result =
                    actionLogService.getActionLogs("user-123", "CREATE_USER", "User", fromDate, toDate, pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }
    }

    @Nested
    @DisplayName("getActionLogById tests")
    class GetActionLogByIdTests {

        @Test
        @DisplayName("Nên trả về action log khi tìm thấy")
        void shouldGetActionLogById() {
            // Arrange
            when(actionLogRepository.findById("log-123")).thenReturn(Optional.of(testActionLog));
            when(actionLogMapper.toResponse(testActionLog)).thenReturn(actionLogResponse);

            // Act
            ActionLogResponse result = actionLogService.getActionLogById("log-123");

            // Assert
            assertNotNull(result);
            assertEquals("log-123", result.getId());
            assertEquals("user-123", result.getUserId());
            assertEquals("Test User", result.getUserDisplayName());
            assertEquals(ActionCode.CREATE_USER, result.getAction());
        }

        @Test
        @DisplayName("Nên ném ResponseStatusException khi action log không tồn tại")
        void shouldThrowExceptionWhenActionLogNotFound() {
            // Arrange
            when(actionLogRepository.findById("non-existent")).thenReturn(Optional.empty());

            // Act & Assert
            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class, () -> actionLogService.getActionLogById("non-existent"));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertTrue(exception.getMessage().contains("Action log not found"));
        }
    }
}
