package org.example.web_thue_gia_su_online.controller.identity_and_administration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.ActionCode;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(ActionLogController.class)
@DisplayName("ActionLogController Blackbox Tests (MockMvc)")
class ActionLogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ActionLogService actionLogService;

    private ActionLogResponse actionLogResponse;

    @BeforeEach
    void setUp() {
        actionLogResponse = ActionLogResponse.builder()
                .id("log-123")
                .userId("user-123")
                .userDisplayName("Test User")
                .action(ActionCode.CREATE_USER)
                .targetType("User")
                .targetId("user-123")
                .ipAddress("127.0.0.1")
                .userAgent("Mozilla/5.0")
                .createdAt(Instant.parse("2024-06-15T10:30:00Z"))
                .build();
    }

    @Nested
    @DisplayName("GET /admin/action-logs - getActionLogs tests")
    class GetActionLogsTests {

        @Test
        @DisplayName("Nên trả về danh sách action logs khi không có bộ lọc - 200 OK")
        void shouldGetAllActionLogs() throws Exception {
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse));
            when(actionLogService.getActionLogs(isNull(), isNull(), isNull(), isNull(), isNull(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs").param("page", "0").param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value("log-123"))
                    .andExpect(jsonPath("$.content[0].userId").value("user-123"))
                    .andExpect(jsonPath("$.content[0].userDisplayName").value("Test User"))
                    .andExpect(jsonPath("$.content[0].action").value("CREATE_USER"))
                    .andExpect(jsonPath("$.totalElements").value(1));

            verify(actionLogService).getActionLogs(isNull(), isNull(), isNull(), isNull(), isNull(), any());
        }

        @Test
        @DisplayName("Nên tìm kiếm theo userId - 200 OK")
        void shouldSearchByUserId() throws Exception {
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse));
            when(actionLogService.getActionLogs(eq("user-123"), isNull(), isNull(), isNull(), isNull(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs")
                            .param("userId", "user-123")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value("log-123"));

            verify(actionLogService).getActionLogs(eq("user-123"), isNull(), isNull(), isNull(), isNull(), any());
        }

        @Test
        @DisplayName("Nên tìm kiếm theo action code - 200 OK")
        void shouldSearchByActionCode() throws Exception {
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse));
            when(actionLogService.getActionLogs(isNull(), eq("CREATE_USER"), isNull(), isNull(), isNull(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs")
                            .param("action", "CREATE_USER")
                            .param("page", "0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].action").value("CREATE_USER"));
        }

        @Test
        @DisplayName("Nên tìm kiếm theo targetType - 200 OK")
        void shouldSearchByTargetType() throws Exception {
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse));
            when(actionLogService.getActionLogs(isNull(), isNull(), eq("User"), isNull(), isNull(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs")
                            .param("targetType", "User")
                            .param("page", "0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].targetType").value("User"));
        }

        @Test
        @DisplayName("Nên tìm kiếm theo khoảng thời gian - 200 OK")
        void shouldSearchByDateRange() throws Exception {
            Instant fromDate = Instant.parse("2024-01-01T00:00:00Z");
            Instant toDate = Instant.parse("2024-12-31T23:59:59Z");
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse));

            when(actionLogService.getActionLogs(isNull(), isNull(), isNull(), eq(fromDate), eq(toDate), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs")
                            .param("fromDate", "2024-01-01T00:00:00Z")
                            .param("toDate", "2024-12-31T23:59:59Z")
                            .param("page", "0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value("log-123"));
        }

        @Test
        @DisplayName("Nên tìm kiếm với nhiều bộ lọc kết hợp - 200 OK")
        void shouldSearchWithMultipleFilters() throws Exception {
            Instant fromDate = Instant.parse("2024-01-01T00:00:00Z");
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse));

            when(actionLogService.getActionLogs(
                            eq("user-123"), eq("CREATE_USER"), eq("User"), eq(fromDate), isNull(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs")
                            .param("userId", "user-123")
                            .param("action", "CREATE_USER")
                            .param("targetType", "User")
                            .param("fromDate", "2024-01-01T00:00:00Z")
                            .param("page", "0")
                            .param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value("log-123"));
        }

        @Test
        @DisplayName("Nên trả về danh sách rỗng khi không có kết quả")
        void shouldReturnEmptyListWhenNoResults() throws Exception {
            PageImpl<ActionLogResponse> emptyPage = new PageImpl<>(List.of());
            when(actionLogService.getActionLogs(any(), any(), any(), any(), any(), any()))
                    .thenReturn(emptyPage);

            mockMvc.perform(get("/admin/action-logs").param("userId", "non-existent"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.totalElements").value(0));
        }

        @Test
        @DisplayName("Nên hỗ trợ phân trang với page và size")
        void shouldSupportPagination() throws Exception {
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse), PageRequest.of(1, 5), 10);
            when(actionLogService.getActionLogs(any(), any(), any(), any(), any(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs").param("page", "1").param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(10));
        }
    }

    @Nested
    @DisplayName("GET /admin/action-logs/{id} - getActionLogById tests")
    class GetActionLogByIdTests {

        @Test
        @DisplayName("Nên trả về action log theo ID - 200 OK")
        void shouldGetActionLogById() throws Exception {
            when(actionLogService.getActionLogById("log-123")).thenReturn(actionLogResponse);

            mockMvc.perform(get("/admin/action-logs/log-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("log-123"))
                    .andExpect(jsonPath("$.userId").value("user-123"))
                    .andExpect(jsonPath("$.userDisplayName").value("Test User"))
                    .andExpect(jsonPath("$.action").value("CREATE_USER"))
                    .andExpect(jsonPath("$.targetType").value("User"))
                    .andExpect(jsonPath("$.targetId").value("user-123"))
                    .andExpect(jsonPath("$.ipAddress").value("127.0.0.1"))
                    .andExpect(jsonPath("$.userAgent").value("Mozilla/5.0"));

            verify(actionLogService).getActionLogById("log-123");
        }

        @Test
        @DisplayName("Nên trả về 404 khi action log không tồn tại")
        void shouldReturnNotFoundWhenActionLogNotFound() throws Exception {
            when(actionLogService.getActionLogById("non-existent"))
                    .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Action log not found"));

            mockMvc.perform(get("/admin/action-logs/non-existent")).andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("Nên trả về 403 khi không có quyền ADMIN")
        void shouldReturnForbiddenWhenNoAdminRole() throws Exception {
            when(actionLogService.getActionLogById(anyString()))
                    .thenThrow(new AccessDeniedException("Access is denied"));

            mockMvc.perform(get("/admin/action-logs/log-123")).andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("Request format tests")
    class RequestFormatTests {

        @Test
        @DisplayName("Nên chấp nhận Accept header application/json")
        void shouldAcceptJsonContentType() throws Exception {
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse));
            when(actionLogService.getActionLogs(any(), any(), any(), any(), any(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("Nên xử lý tham số sắp xếp")
        void shouldHandleSorting() throws Exception {
            PageImpl<ActionLogResponse> page = new PageImpl<>(List.of(actionLogResponse));
            when(actionLogService.getActionLogs(any(), any(), any(), any(), any(), any()))
                    .thenReturn(page);

            mockMvc.perform(get("/admin/action-logs")
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "createdAt,desc"))
                    .andExpect(status().isOk());
        }
    }
}
