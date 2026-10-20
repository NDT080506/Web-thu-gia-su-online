package org.example.web_thue_gia_su_online.service.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.example.web_thue_gia_su_online.service.authentication.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl service;

    @Test
    void sendVerificationEmail_shouldSucceed() {
        service.sendVerificationEmail("to@example.com", "token-123");

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
