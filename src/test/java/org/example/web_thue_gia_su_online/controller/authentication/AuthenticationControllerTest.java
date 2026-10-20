package org.example.web_thue_gia_su_online.controller.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.web_thue_gia_su_online.dto.request.authentication.AuthenticationRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.IntrospectRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.LogoutRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.RefreshTokenRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.AuthenticationResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.IntrospectResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.RefreshTokenResponse;
import org.example.web_thue_gia_su_online.service.authentication.AuthenticationService;
import org.example.web_thue_gia_su_online.service.authentication.EmailVerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private EmailVerificationService emailVerificationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void introspect_shouldReturnIntrospectResponse() {
        IntrospectRequest request =
                IntrospectRequest.builder().accessToken("test-token").build();
        IntrospectResponse response = IntrospectResponse.builder().valid(true).build();
        when(authenticationService.introspect(eq(request))).thenReturn(response);

        ApiResponse<IntrospectResponse> apiResponse = authenticationController.introspect(request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(authenticationService).introspect(eq(request));
    }

    @Test
    void authenticate_shouldReturnAuthenticationResponse() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .displayName("user")
                .password("pass")
                .build();
        AuthenticationResponse response =
                AuthenticationResponse.builder().accessToken("token").build();
        when(authenticationService.authenticate(eq(request))).thenReturn(response);

        ApiResponse<AuthenticationResponse> apiResponse = authenticationController.authenticate(request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(authenticationService).authenticate(eq(request));
    }

    @Test
    void logout_shouldReturnSuccessMessage() {
        LogoutRequest request = LogoutRequest.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();
        doNothing().when(authenticationService).logout(eq(request));

        ApiResponse<Void> apiResponse = authenticationController.logout(request);

        assertNotNull(apiResponse);
        assertEquals("Token has been log out", apiResponse.getMessage());
        verify(authenticationService).logout(eq(request));
    }

    @Test
    void refreshToken_shouldReturnRefreshTokenResponse() throws Exception {
        RefreshTokenRequest request =
                RefreshTokenRequest.builder().token("old-token").build();
        RefreshTokenResponse response =
                RefreshTokenResponse.builder().accessToken("new-token").build();
        when(authenticationService.refreshToken(eq(request))).thenReturn(response);

        ApiResponse<RefreshTokenResponse> apiResponse = authenticationController.refreshToken(request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(authenticationService).refreshToken(eq(request));
    }

    @Test
    void verifyEmail_shouldReturnSuccessMessage() {
        doNothing().when(emailVerificationService).verifyEmail(eq("token"));

        ApiResponse<Void> apiResponse = authenticationController.verifyEmail("token");

        assertNotNull(apiResponse);
        assertEquals("Email verified successfully", apiResponse.getMessage());
        verify(emailVerificationService).verifyEmail("token");
    }
}
