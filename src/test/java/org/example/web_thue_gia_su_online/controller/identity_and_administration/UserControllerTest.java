package org.example.web_thue_gia_su_online.controller.identity_and_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateRequest;
import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.UserUpdateStatusRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;
import org.example.web_thue_gia_su_online.service.identity_and_administration.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_shouldReturnResponse() {
        UserCreationRequest request = UserCreationRequest.builder()
                .email("test@example.com")
                .password("password123")
                .displayName("Test User")
                .build();
        UserResponse response = UserResponse.builder().id("1").build();
        when(userService.createUser(eq(request))).thenReturn(response);
        ApiResponse<UserResponse> apiResponse = userController.createUser(request);
        assertNotNull(apiResponse);
        verify(userService).createUser(eq(request));
    }

    @Test
    void getMyUser_shouldReturnResponse() {
        UserResponse response = UserResponse.builder().id("1").build();
        when(userService.getMyUser()).thenReturn(response);
        ApiResponse<UserResponse> apiResponse = userController.getMyUser();
        assertNotNull(apiResponse);
        verify(userService).getMyUser();
    }

    @Test
    void getUsers_shouldReturnList() {
        List<UserResponse> list = List.of(UserResponse.builder().id("1").build());
        when(userService.getUsers()).thenReturn(list);
        ApiResponse<List<UserResponse>> apiResponse = userController.getUsers();
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void getUser_shouldReturnResponse() {
        UserResponse response = UserResponse.builder().id("1").build();
        when(userService.getUser("1")).thenReturn(response);
        ApiResponse<UserResponse> apiResponse = userController.getUser("1");
        assertNotNull(apiResponse);
        verify(userService).getUser("1");
    }

    @Test
    void updateUser_shouldReturnResponse() {
        UserUpdateRequest request =
                UserUpdateRequest.builder().displayName("Updated User").build();
        UserResponse response = UserResponse.builder().id("1").build();
        when(userService.updateUser(eq(request), eq("1"))).thenReturn(response);
        ApiResponse<UserResponse> apiResponse = userController.updateUser(request, "1");
        assertNotNull(apiResponse);
        verify(userService).updateUser(eq(request), eq("1"));
    }

    @Test
    void updateUserAccountStatus_shouldReturnResponse() {
        UserUpdateStatusRequest request = UserUpdateStatusRequest.builder().build();
        UserResponse response = UserResponse.builder().id("1").build();
        when(userService.updateUserAccountStatus(eq(request), eq("1"))).thenReturn(response);
        ApiResponse<UserResponse> apiResponse = userController.updateUserAccountStatus(request, "1");
        assertNotNull(apiResponse);
        verify(userService).updateUserAccountStatus(eq(request), eq("1"));
    }

    @Test
    void deleteUser_shouldReturnSuccessMessage() {
        doNothing().when(userService).deleteUser("1");
        ApiResponse<Void> apiResponse = userController.deleteUser("1");
        assertNotNull(apiResponse);
        assertEquals("User has been deleted", apiResponse.getMessage());
    }
}
