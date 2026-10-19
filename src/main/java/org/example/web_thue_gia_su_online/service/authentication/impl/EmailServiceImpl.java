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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl implements EmailService {

    JavaMailSender mailSender;

    @NonFinal
    @Value("${app.frontend-url}")
    String frontendUrl;

    @Override
    public void sendVerificationEmail(String email, String token) {
        String link = frontendUrl + "/verify-email?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your email");
        message.setText("Click the link below to verify your email:\n" + link + "\nThis link expires in 15 minutes.");

        mailSender.send(message);
    }
}
