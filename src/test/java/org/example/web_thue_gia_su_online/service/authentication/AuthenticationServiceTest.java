package org.example.web_thue_gia_su_online.service.authentication;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.authentication.AuthenticationRequest;
import org.example.web_thue_gia_su_online.dto.request.authentication.IntrospectRequest;
import org.example.web_thue_gia_su_online.dto.response.authentication.AuthenticationResponse;
import org.example.web_thue_gia_su_online.dto.response.authentication.IntrospectResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.AccountStatus;
import org.example.web_thue_gia_su_online.repository.authentication.InvalidatedTokenRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.authentication.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @BeforeEach
    void setUp() throws Exception {
        Field signerKeyField = AuthenticationServiceImpl.class.getDeclaredField("SIGNER_KEY");
        signerKeyField.setAccessible(true);
        signerKeyField.set(service, "test-signer-key-that-is-long-enough-for-jwt-signing-purpose");

        Field validDurationField = AuthenticationServiceImpl.class.getDeclaredField("VALID_DURATION");
        validDurationField.setAccessible(true);
        validDurationField.setLong(service, 3600);

        Field refreshableDurationField = AuthenticationServiceImpl.class.getDeclaredField("REFRESHABLE_DURATION");
        refreshableDurationField.setAccessible(true);
        refreshableDurationField.setLong(service, 86400);
    }

    @Test
    void authenticate_shouldReturnResponse() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .displayName("testuser")
                .password("password123")
                .build();
        User user = User.builder()
                .id("1")
                .displayName("testuser")
                .passwordHash("encoded")
                .status(AccountStatus.ACTIVE)
                .emailVerifiedAt(java.time.Instant.now())
                .build();
        when(userRepository.findByDisplayName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq("password123"), eq("encoded"))).thenReturn(true);

        AuthenticationResponse result = service.authenticate(request);

        assertNotNull(result);
        assertNotNull(result.getAccessToken());
    }

    @Test
    void authenticate_userNotFound_shouldThrow() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .displayName("notfound")
                .password("password123")
                .build();
        when(userRepository.findByDisplayName(anyString())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.authenticate(request));
    }

    @Test
    void authenticate_invalidPassword_shouldThrow() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .displayName("testuser")
                .password("wrongpassword")
                .build();
        User user = User.builder()
                .id("1")
                .displayName("testuser")
                .passwordHash("encoded")
                .status(AccountStatus.ACTIVE)
                .emailVerifiedAt(java.time.Instant.now())
                .build();
        when(userRepository.findByDisplayName(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq("wrongpassword"), eq("encoded"))).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.authenticate(request));
    }

    @Test
    void introspect_shouldReturnValidResponse() {
        IntrospectRequest request =
                IntrospectRequest.builder().accessToken("test-token").build();
        IntrospectResponse result = service.introspect(request);
        assertNotNull(result);
        // With an invalid token string, the result should have valid=false
        assertFalse(result.isValid());
    }
}
