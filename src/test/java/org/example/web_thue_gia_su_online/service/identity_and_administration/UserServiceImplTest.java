package org.example.web_thue_gia_su_online.service.identity_and_administration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateStatusRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.AccountStatus;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.UserRole;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.UserMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.authentication.EmailVerificationService;
import org.example.web_thue_gia_su_online.service.identity_and_administration.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Unit Tests")
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    EmailVerificationService emailVerificationService;

    @Mock
    ActionLogService actionLogService;

    @InjectMocks
    UserServiceImpl userService;

    private User testUser;
    private UserCreationRequest creationRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .displayName("Test User")
                .passwordHash("$2a$encoded")
                .role(UserRole.STUDENT)
                .status(AccountStatus.ACTIVE)
                .phone("0123456789")
                .avatarUrl("https://example.com/avatar.png")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        creationRequest = UserCreationRequest.builder()
                .email("new@example.com")
                .password("password123")
                .displayName("New User")
                .role(UserRole.STUDENT)
                .phone("0987654321")
                .build();

        userResponse = UserResponse.builder()
                .id("user-123")
                .email("test@example.com")
                .displayName("Test User")
                .role(UserRole.STUDENT)
                .status(AccountStatus.ACTIVE)
                .phone("0123456789")
                .avatarUrl("https://example.com/avatar.png")
                .createdAt(testUser.getCreatedAt())
                .updatedAt(testUser.getUpdatedAt())
                .build();
    }

    @Nested
    @DisplayName("createUser tests")
    class CreateUserTests {

        @Test
        @DisplayName("Nên tạo user thành công khi email và displayName chưa tồn tại")
        void shouldCreateUserSuccessfully() {
            // Arrange
            User mappedUser = User.builder()
                    .email(creationRequest.getEmail())
                    .displayName(creationRequest.getDisplayName())
                    .role(creationRequest.getRole())
                    .phone(creationRequest.getPhone())
                    .build();

            User savedUser = User.builder()
                    .id("new-user-id")
                    .email(creationRequest.getEmail())
                    .displayName(creationRequest.getDisplayName())
                    .role(UserRole.STUDENT)
                    .status(AccountStatus.ACTIVE)
                    .phone(creationRequest.getPhone())
                    .createdAt(Instant.now())
                    .build();

            UserResponse expectedResponse = UserResponse.builder()
                    .id("new-user-id")
                    .email(creationRequest.getEmail())
                    .displayName(creationRequest.getDisplayName())
                    .role(UserRole.STUDENT)
                    .status(AccountStatus.ACTIVE)
                    .build();

            when(userRepository.existsByEmail(creationRequest.getEmail())).thenReturn(false);
            when(userRepository.existsByDisplayName(creationRequest.getDisplayName()))
                    .thenReturn(false);
            when(userMapper.toUser(creationRequest)).thenReturn(mappedUser);
            when(passwordEncoder.encode(creationRequest.getPassword())).thenReturn("$2a$encoded123");
            when(userRepository.saveAndFlush(any(User.class))).thenReturn(savedUser);
            when(userMapper.toUserResponse(savedUser)).thenReturn(expectedResponse);

            // Act
            UserResponse result = userService.createUser(creationRequest);

            // Assert
            assertNotNull(result);
            assertEquals("new-user-id", result.getId());
            assertEquals(creationRequest.getEmail(), result.getEmail());
            assertEquals(creationRequest.getDisplayName(), result.getDisplayName());

            verify(userRepository).existsByEmail(creationRequest.getEmail());
            verify(userRepository).existsByDisplayName(creationRequest.getDisplayName());
            verify(passwordEncoder).encode(creationRequest.getPassword());
            verify(userRepository).saveAndFlush(any(User.class));
            verify(emailVerificationService).sendVerificationEmail(any(User.class));
            verify(actionLogService).log(any(), any(), anyString(), anyString());
        }

        @Test
        @DisplayName("Nên ném AppException khi email đã tồn tại")
        void shouldThrowExceptionWhenEmailExists() {
            // Arrange
            when(userRepository.existsByEmail(creationRequest.getEmail())).thenReturn(true);

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> userService.createUser(creationRequest));
            assertEquals(ErrorCode.EMAIL_EXISTED, exception.getErrorCode());

            verify(userRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("Nên ném AppException khi displayName đã tồn tại")
        void shouldThrowExceptionWhenDisplayNameExists() {
            // Arrange
            when(userRepository.existsByEmail(creationRequest.getEmail())).thenReturn(false);
            when(userRepository.existsByDisplayName(creationRequest.getDisplayName()))
                    .thenReturn(true);

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> userService.createUser(creationRequest));
            assertEquals(ErrorCode.USER_EXISTED, exception.getErrorCode());

            verify(userRepository, never()).saveAndFlush(any());
        }

        @Test
        @DisplayName("Nên ném AppException khi xảy ra DataIntegrityViolationException")
        void shouldThrowExceptionOnDataIntegrityViolation() {
            // Arrange
            User mappedUser = User.builder()
                    .email(creationRequest.getEmail())
                    .displayName(creationRequest.getDisplayName())
                    .build();

            when(userRepository.existsByEmail(creationRequest.getEmail())).thenReturn(false);
            when(userRepository.existsByDisplayName(creationRequest.getDisplayName()))
                    .thenReturn(false);
            when(userMapper.toUser(creationRequest)).thenReturn(mappedUser);
            when(passwordEncoder.encode(creationRequest.getPassword())).thenReturn("$2a$encoded");
            when(userRepository.saveAndFlush(any(User.class)))
                    .thenThrow(new DataIntegrityViolationException("Duplicate key"));

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> userService.createUser(creationRequest));
            assertEquals(ErrorCode.USER_EXISTED, exception.getErrorCode());
        }

        @Test
        @DisplayName("Nên đặt passwordHash đã encode và emailVerifiedAt = null")
        void shouldEncodePasswordAndSetEmailVerifiedAtNull() {
            // Arrange
            User mappedUser = User.builder()
                    .email(creationRequest.getEmail())
                    .displayName(creationRequest.getDisplayName())
                    .build();

            User savedUser = User.builder()
                    .id("new-id")
                    .email(creationRequest.getEmail())
                    .displayName(creationRequest.getDisplayName())
                    .build();

            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByDisplayName(anyString())).thenReturn(false);
            when(userMapper.toUser(creationRequest)).thenReturn(mappedUser);
            when(passwordEncoder.encode(anyString())).thenReturn("$2a$encoded");
            when(userRepository.saveAndFlush(any(User.class))).thenReturn(savedUser);
            when(userMapper.toUserResponse(savedUser)).thenReturn(userResponse);

            // Act
            userService.createUser(creationRequest);

            // Assert
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).saveAndFlush(userCaptor.capture());

            User capturedUser = userCaptor.getValue();
            assertEquals("$2a$encoded", capturedUser.getPasswordHash());
            assertNull(capturedUser.getEmailVerifiedAt());
        }
    }

    @Nested
    @DisplayName("getUsers tests")
    class GetUsersTests {

        @Test
        @DisplayName("Nên trả về danh sách user khi có quyền ADMIN")
        void shouldGetUsersPage() {
            // Arrange
            Pageable pageable = PageRequest.of(0, 10);
            Page<UserResponse> expectedPage = new PageImpl<>(java.util.List.of(userResponse));
            when(userRepository.findResponses(pageable)).thenReturn(expectedPage);

            // Act
            Page<UserResponse> result = userService.getUsers(pageable);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals("user-123", result.getContent().getFirst().getId());
            verify(userRepository).findResponses(pageable);
        }
    }

    @Nested
    @DisplayName("getMyUser tests")
    class GetMyUserTests {

        @Test
        @DisplayName("Nên trả về thông tin user hiện tại từ SecurityContext")
        void shouldGetMyUser() {
            // Arrange
            String currentUserId = "user-123";
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(currentUserId);
            SecurityContextHolder.setContext(securityContext);

            when(userRepository.findById(currentUserId)).thenReturn(Optional.of(testUser));
            when(userMapper.toUserResponse(testUser)).thenReturn(userResponse);

            // Act
            UserResponse result = userService.getMyUser();

            // Assert
            assertNotNull(result);
            assertEquals(currentUserId, result.getId());
            assertEquals("test@example.com", result.getEmail());

            SecurityContextHolder.clearContext();
        }

        @Test
        @DisplayName("Nên ném AppException khi user không tìm thấy trong SecurityContext")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            String currentUserId = "non-existent";
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getName()).thenReturn(currentUserId);
            SecurityContextHolder.setContext(securityContext);

            when(userRepository.findById(currentUserId)).thenReturn(Optional.empty());

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> userService.getMyUser());
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

            SecurityContextHolder.clearContext();
        }
    }

    @Nested
    @DisplayName("getUser tests")
    class GetUserTests {

        @Test
        @DisplayName("Nên trả về user khi tìm thấy")
        void shouldGetUserById() {
            // Arrange
            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            when(userMapper.toUserResponse(testUser)).thenReturn(userResponse);

            // Act
            UserResponse result = userService.getUser("user-123");

            // Assert
            assertNotNull(result);
            assertEquals("user-123", result.getId());
        }

        @Test
        @DisplayName("Nên ném AppException khi user không tồn tại")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            when(userRepository.findById("non-existent")).thenReturn(Optional.empty());

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> userService.getUser("non-existent"));
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("updateUser tests")
    class UpdateUserTests {

        @Test
        @DisplayName("Nên cập nhật user thành công")
        void shouldUpdateUserSuccessfully() {
            // Arrange
            UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                    .displayName("Updated Name")
                    .phone("0999999999")
                    .build();

            User updatedUser = User.builder()
                    .id("user-123")
                    .email("test@example.com")
                    .displayName("Updated Name")
                    .phone("0999999999")
                    .role(UserRole.STUDENT)
                    .status(AccountStatus.ACTIVE)
                    .build();

            UserResponse expectedResponse = UserResponse.builder()
                    .id("user-123")
                    .displayName("Updated Name")
                    .phone("0999999999")
                    .build();

            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(userMapper.toUserResponse(any(User.class))).thenReturn(expectedResponse);

            // Act
            UserResponse result = userService.updateUser(updateRequest, "user-123");

            // Assert
            assertNotNull(result);
            assertEquals("Updated Name", result.getDisplayName());
            assertEquals("0999999999", result.getPhone());

            verify(userMapper).updateUser(eq(testUser), eq(updateRequest));
            verify(userRepository).save(any(User.class));
            verify(actionLogService).log(any(), any(), anyString(), anyString());
        }

        @Test
        @DisplayName("Nên ném AppException khi user cần cập nhật không tồn tại")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            when(userRepository.findById("non-existent")).thenReturn(Optional.empty());
            UserUpdateRequest updateRequest =
                    UserUpdateRequest.builder().displayName("Updated").build();

            // Act & Assert
            AppException exception =
                    assertThrows(AppException.class, () -> userService.updateUser(updateRequest, "non-existent"));
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("updateUserAccountStatus tests")
    class UpdateUserAccountStatusTests {

        @Test
        @DisplayName("Nên cập nhật trạng thái tài khoản thành công")
        void shouldUpdateStatusSuccessfully() {
            // Arrange
            UserUpdateStatusRequest request = UserUpdateStatusRequest.builder()
                    .status(AccountStatus.SUSPENDED)
                    .build();

            User updatedUser = User.builder()
                    .id("user-123")
                    .email("test@example.com")
                    .displayName("Test User")
                    .status(AccountStatus.SUSPENDED)
                    .role(UserRole.STUDENT)
                    .build();

            UserResponse expectedResponse = UserResponse.builder()
                    .id("user-123")
                    .status(AccountStatus.SUSPENDED)
                    .build();

            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(userMapper.toUserResponse(any(User.class))).thenReturn(expectedResponse);

            // Act
            UserResponse result = userService.updateUserAccountStatus(request, "user-123");

            // Assert
            assertNotNull(result);
            assertEquals(AccountStatus.SUSPENDED, result.getStatus());
            verify(actionLogService).log(any(), any(), anyString(), anyString());
        }

        @Test
        @DisplayName("Nên ném AppException khi user không tồn tại")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            when(userRepository.findById("non-existent")).thenReturn(Optional.empty());
            UserUpdateStatusRequest request = UserUpdateStatusRequest.builder()
                    .status(AccountStatus.BANNED)
                    .build();

            // Act & Assert
            AppException exception = assertThrows(
                    AppException.class, () -> userService.updateUserAccountStatus(request, "non-existent"));
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("Nên chuyển trạng thái sang BANNED")
        void shouldBanUser() {
            // Arrange
            UserUpdateStatusRequest request = UserUpdateStatusRequest.builder()
                    .status(AccountStatus.BANNED)
                    .build();

            User bannedUser =
                    User.builder().id("user-123").status(AccountStatus.BANNED).build();

            UserResponse expectedResponse = UserResponse.builder()
                    .id("user-123")
                    .status(AccountStatus.BANNED)
                    .build();

            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(bannedUser);
            when(userMapper.toUserResponse(any(User.class))).thenReturn(expectedResponse);

            // Act
            UserResponse result = userService.updateUserAccountStatus(request, "user-123");

            // Assert
            assertEquals(AccountStatus.BANNED, result.getStatus());
        }
    }

    @Nested
    @DisplayName("deleteUser tests")
    class DeleteUserTests {

        @Test
        @DisplayName("Nên xóa user thành công")
        void shouldDeleteUserSuccessfully() {
            // Arrange
            when(userRepository.findById("user-123")).thenReturn(Optional.of(testUser));
            doNothing().when(userRepository).delete(testUser);

            // Act
            userService.deleteUser("user-123");

            // Assert
            verify(userRepository).delete(testUser);
            verify(actionLogService).log(any(), any(), anyString(), anyString());
        }

        @Test
        @DisplayName("Nên ném AppException khi user cần xóa không tồn tại")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            when(userRepository.findById("non-existent")).thenReturn(Optional.empty());

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> userService.deleteUser("non-existent"));
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

            verify(userRepository, never()).delete(any());
        }
    }
}
