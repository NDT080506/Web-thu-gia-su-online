package org.example.web_thue_gia_su_online.service.authentication.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

import org.example.web_thue_gia_su_online.entity.authentication.EmailVerificationToken;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.repository.authentication.EmailVerificationTokenRepository;
import org.example.web_thue_gia_su_online.service.authentication.EmailService;
import org.example.web_thue_gia_su_online.service.authentication.EmailVerificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailVerificationServiceImpl implements EmailVerificationService {

    EmailVerificationTokenRepository tokenRepository;
    EmailService emailService;

    private String generateToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void sendVerificationEmail(User user) {
        String rawToken = generateToken();

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .user(user)
                .tokenHash(hashToken(rawToken))
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .build();

        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(user.getEmail(), rawToken);
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        String tokenHash = hashToken(token);

        EmailVerificationToken verificationToken = tokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        if (verificationToken.getUsedAt() != null
                || !verificationToken.getExpiresAt().isAfter(Instant.now())) {
            throw new IllegalArgumentException("Verification token expired or already used");
        }

        User user = verificationToken.getUser();

        user.setEmailVerifiedAt(Instant.now());
        verificationToken.setUsedAt(Instant.now());
    }
}
