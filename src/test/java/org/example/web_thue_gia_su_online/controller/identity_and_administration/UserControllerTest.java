package org.example.web_thue_gia_su_online.controller.identity_and_administration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateStatusRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.AccountStatus;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.UserRole;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.exception.GlobalExceptionHandler;
import org.example.web_thue_gia_su_online.service.identity_and_administration.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("UserController Blackbox Tests (MockMvc)")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    private UserCreationRequest validCreationRequest;
    private UserUpdateRequest validUpdateRequest;
    private UserUpdateStatusRequest validStatusRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        validCreationRequest = UserCreationRequest.builder()
                .email("new@example.com")
                .password("password123")
                .displayName("New User")
                .role(UserRole.STUDENT)
                .phone("0987654321")
                .build();

        validUpdateRequest = UserUpdateRequest.builder()
                .displayName("Updated Name")
                .phone("0999999999")
                .build();

        validStatusRequest = UserUpdateStatusRequest.builder()
                .status(AccountStatus.SUSPENDED)
                .build();

        userResponse = UserResponse.builder()
                .id("user-123")
                .email("test@example.com")
                .displayName("Test User")
                .role(UserRole.STUDENT)
                .status(AccountStatus.ACTIVE)
                .phone("0123456789")
                .avatarUrl("https://example.com/avatar.png")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Nested
    @DisplayName("POST /users - createUser tests")
    class CreateUserTests {

        @Test
        @DisplayName("Nên tạo user thành công - trả về 200 OK với UserResponse")
        void shouldCreateUserSuccessfully() throws Exception {
            when(userService.createUser(any(UserCreationRequest.class))).thenReturn(userResponse);

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreationRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(1000))
                    .andExpect(jsonPath("$.result.id").value("user-123"))
                    .andExpect(jsonPath("$.result.email").value("test@example.com"))
                    .andExpect(jsonPath("$.result.displayName").value("Test User"));

            verify(userService).createUser(any(UserCreationRequest.class));
        }

        @Test
        @DisplayName("Nên trả về 400 khi email trống")
        void shouldReturnBadRequestWhenEmailEmpty() throws Exception {
            UserCreationRequest invalidRequest = UserCreationRequest.builder()
                    .email("")
                    .password("password123")
                    .displayName("Test")
                    .build();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).createUser(any());
        }

        @Test
        @DisplayName("Nên trả về 400 khi password quá ngắn")
        void shouldReturnBadRequestWhenPasswordTooShort() throws Exception {
            UserCreationRequest invalidRequest = UserCreationRequest.builder()
                    .email("test@example.com")
                    .password("123")
                    .displayName("Test")
                    .build();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).createUser(any());
        }

        @Test
        @DisplayName("Nên trả về 400 khi displayName trống")
        void shouldReturnBadRequestWhenDisplayNameEmpty() throws Exception {
            UserCreationRequest invalidRequest = UserCreationRequest.builder()
                    .email("test@example.com")
                    .password("password123")
                    .displayName("")
                    .build();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).createUser(any());
        }

        @Test
        @DisplayName("Nên trả về 400 khi email không hợp lệ")
        void shouldReturnBadRequestWhenEmailInvalid() throws Exception {
            UserCreationRequest invalidRequest = UserCreationRequest.builder()
                    .email("not-an-email")
                    .password("password123")
                    .displayName("Test")
                    .build();

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(userService, never()).createUser(any());
        }

        @Test
        @DisplayName("Nên trả về 400 khi email đã tồn tại")
        void shouldReturnBadRequestWhenEmailExists() throws Exception {
            when(userService.createUser(any(UserCreationRequest.class)))
                    .thenThrow(new AppException(ErrorCode.EMAIL_EXISTED));

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreationRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(1015))
                    .andExpect(jsonPath("$.message").value("Email existed"));
        }

        @Test
        @DisplayName("Nên trả về 400 khi displayName đã tồn tại")
        void shouldReturnBadRequestWhenDisplayNameExists() throws Exception {
            when(userService.createUser(any(UserCreationRequest.class)))
                    .thenThrow(new AppException(ErrorCode.USER_EXISTED));

            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreationRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(1001))
                    .andExpect(jsonPath("$.message").value("User existed"));
        }
    }

    @Nested
    @DisplayName("GET /users - getMyUser tests")
    class GetMyUserTests {

        @Test
        @DisplayName("Nên trả về thông tin user hiện tại - 200 OK")
        void shouldGetMyUser() throws Exception {
            when(userService.getMyUser()).thenReturn(userResponse);

            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(1000))
                    .andExpect(jsonPath("$.result.id").value("user-123"))
                    .andExpect(jsonPath("$.result.email").value("test@example.com"));

            verify(userService).getMyUser();
        }

        @Test
        @DisplayName("Nên trả về 404 khi user không tìm thấy")
        void shouldReturnNotFoundWhenUserNotFound() throws Exception {
            when(userService.getMyUser()).thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));

            mockMvc.perform(get("/users"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(1002));
        }
    }

    @Nested
    @DisplayName("GET /users/admin - getUsers tests")
    class GetUsersTests {

        @Test
        @DisplayName("Nên trả về danh sách user phân trang - 200 OK")
        void shouldGetUsersPage() throws Exception {
            PageImpl<UserResponse> page = new PageImpl<>(java.util.List.of(userResponse));
            when(userService.getUsers(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/users/admin").param("page", "0").param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result.content").isArray())
                    .andExpect(jsonPath("$.result.totalElements").value(1));

            verify(userService).getUsers(any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("GET /users/admin/{userId} - getUser tests")
    class GetUserTests {

        @Test
        @DisplayName("Nên trả về user theo ID - 200 OK")
        void shouldGetUserById() throws Exception {
            when(userService.getUser("user-123")).thenReturn(userResponse);

            mockMvc.perform(get("/users/admin/user-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(1000))
                    .andExpect(jsonPath("$.result.id").value("user-123"))
                    .andExpect(jsonPath("$.result.displayName").value("Test User"));

            verify(userService).getUser("user-123");
        }

        @Test
        @DisplayName("Nên trả về 404 khi user không tồn tại")
        void shouldReturnNotFoundWhenUserNotFound() throws Exception {
            when(userService.getUser("non-existent")).thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));

            mockMvc.perform(get("/users/admin/non-existent"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(1002));
        }
    }

    @Nested
    @DisplayName("PATCH /users/{userId} - updateUser tests")
    class UpdateUserTests {

        @Test
        @DisplayName("Nên cập nhật user thành công - 200 OK")
        void shouldUpdateUserSuccessfully() throws Exception {
            UserResponse updatedResponse = UserResponse.builder()
                    .id("user-123")
                    .displayName("Updated Name")
                    .phone("0999999999")
                    .build();
            when(userService.updateUser(any(UserUpdateRequest.class), eq("user-123")))
                    .thenReturn(updatedResponse);

            mockMvc.perform(patch("/users/user-123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(1000))
                    .andExpect(jsonPath("$.result.displayName").value("Updated Name"))
                    .andExpect(jsonPath("$.result.phone").value("0999999999"));

            verify(userService).updateUser(any(UserUpdateRequest.class), eq("user-123"));
        }

        @Test
        @DisplayName("Nên trả về 404 khi user cần cập nhật không tồn tại")
        void shouldReturnNotFoundWhenUserNotFound() throws Exception {
            when(userService.updateUser(any(UserUpdateRequest.class), eq("non-existent")))
                    .thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));

            mockMvc.perform(patch("/users/non-existent")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validUpdateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(1002));
        }
    }

    @Nested
    @DisplayName("PUT /users/admin/{userId}/status - updateUserAccountStatus tests")
    class UpdateUserAccountStatusTests {

        @Test
        @DisplayName("Nên cập nhật trạng thái tài khoản thành công - 200 OK")
        void shouldUpdateStatusSuccessfully() throws Exception {
            UserResponse updatedResponse = UserResponse.builder()
                    .id("user-123")
                    .status(AccountStatus.SUSPENDED)
                    .build();
            when(userService.updateUserAccountStatus(any(UserUpdateStatusRequest.class), eq("user-123")))
                    .thenReturn(updatedResponse);

            mockMvc.perform(put("/users/admin/user-123/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validStatusRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(1000))
                    .andExpect(jsonPath("$.result.status").value("SUSPENDED"));

            verify(userService).updateUserAccountStatus(any(UserUpdateStatusRequest.class), eq("user-123"));
        }

        @Test
        @DisplayName("Nên trả về 404 khi user không tồn tại")
        void shouldReturnNotFoundWhenUserNotFound() throws Exception {
            when(userService.updateUserAccountStatus(any(UserUpdateStatusRequest.class), eq("non-existent")))
                    .thenThrow(new AppException(ErrorCode.USER_NOT_FOUND));

            mockMvc.perform(put("/users/admin/non-existent/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validStatusRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(1002));
        }
    }

    @Nested
    @DisplayName("DELETE /users/{userId} - deleteUser tests")
    class DeleteUserTests {

        @Test
        @DisplayName("Nên xóa user thành công - 200 OK")
        void shouldDeleteUserSuccessfully() throws Exception {
            doNothing().when(userService).deleteUser("user-123");

            mockMvc.perform(delete("/users/user-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(1000))
                    .andExpect(jsonPath("$.message").value("User has been deleted"));

            verify(userService).deleteUser("user-123");
        }

        @Test
        @DisplayName("Nên trả về 404 khi user cần xóa không tồn tại")
        void shouldReturnNotFoundWhenUserNotFound() throws Exception {
            doThrow(new AppException(ErrorCode.USER_NOT_FOUND))
                    .when(userService)
                    .deleteUser("non-existent");

            mockMvc.perform(delete("/users/non-existent"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(1002));
        }
    }

    @Nested
    @DisplayName("Access Denied tests")
    class AccessDeniedTests {

        @Test
        @DisplayName("Nên trả về 403 khi không có quyền truy cập")
        void shouldReturnForbiddenWhenAccessDenied() throws Exception {
            when(userService.getUsers(any(Pageable.class))).thenThrow(new AccessDeniedException("Access is denied"));

            mockMvc.perform(get("/users/admin"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.code").value(1007))
                    .andExpect(jsonPath("$.message").value("You do not have permission"));
        }
    }
}
