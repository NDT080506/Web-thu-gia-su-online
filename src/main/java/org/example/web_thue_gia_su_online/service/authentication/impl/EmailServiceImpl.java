package org.example.web_thue_gia_su_online.service.authentication.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.example.web_thue_gia_su_online.service.authentication.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

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

    Resend resend;

    @NonFinal
    @Value("${app.mail.from}")
    String fromEmail;

    @NonFinal
    @Value("${app.backend-url}")
    String backendUrl;

    @Override
    public void sendVerificationEmail(String email, String token) {
        String link = backendUrl + "/giasu/auth/verify-email?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        String content = """
				Chào bạn,

				Vui lòng nhấn vào đường link dưới đây để xác thực tài khoản của bạn:
				%s

				Đường link này sẽ hết hạn trong 15 phút.

				Trân trọng,
				Đội ngũ EduTutor
				""".formatted(link);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(email)
                .subject("Verify your email - EduTutor")
                .text(content)
                .build();

        try {
            log.info("Sending verification email from {} to {}", fromEmail, email);

            resend.emails().send(params);

            log.info("Verification email sent successfully to {}", email);
        } catch (ResendException e) {
            log.error("Failed to send verification email to {}", email, e);
            throw new IllegalStateException("Could not send verification email", e);
        }
    }
}
