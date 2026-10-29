package org.example.web_thue_gia_su_online.service.authentication.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.example.web_thue_gia_su_online.service.authentication.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String fromEmail;

    @NonFinal
    @Value("${app.frontend-url}")
    String frontendUrl;

    @NonFinal
    @Value("${app.backend-url}")
    String backendUrl;

    @Override
    public void sendVerificationEmail(String email, String token) {
        String link = backendUrl + "/giasu/auth/verify-email?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Verify your email - EduTutor");
        message.setText("Chào bạn,\n\nVui lòng nhấn vào đường link dưới đây để xác thực tài khoản của bạn:\n" + link
                + "\n\nĐường link này sẽ hết hạn trong 15 phút.\n\nTrân trọng,\nĐội ngũ EduTutor");

        log.info("Sending verification email from {} to {}", fromEmail, email);
        mailSender.send(message);
        log.info("Verification email sent successfully to {}", email);
    }
}
