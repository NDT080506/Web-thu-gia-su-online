package org.example.web_thue_gia_su_online.service.authentication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.authentication.EmailVerificationToken;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.repository.authentication.EmailVerificationTokenRepository;
import org.example.web_thue_gia_su_online.service.authentication.impl.EmailVerificationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {

    @Mock
    private EmailVerificationTokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailVerificationServiceImpl service;

    @Test
    void sendVerificationEmail_shouldSucceed() {
        User user = User.builder().id("1").email("test@example.com").build();
        EmailVerificationToken token =
                EmailVerificationToken.builder().user(user).build();
        when(tokenRepository.save(any())).thenReturn(token);
        doNothing().when(emailService).sendVerificationEmail(anyString(), anyString());

        assertDoesNotThrow(() -> service.sendVerificationEmail(user));
    }

    @Test
    void verifyEmail_shouldSucceed() {
        User user = User.builder().id("1").email("test@example.com").build();
        EmailVerificationToken token = EmailVerificationToken.builder()
                .user(user)
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .build();

        when(tokenRepository.findByTokenHash(anyString())).thenReturn(Optional.of(token));

        assertDoesNotThrow(() -> service.verifyEmail("token"));
    }

    @Test
    void verifyEmail_invalidToken_shouldThrow() {
        when(tokenRepository.findByTokenHash(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.verifyEmail("token"));
    }
}
