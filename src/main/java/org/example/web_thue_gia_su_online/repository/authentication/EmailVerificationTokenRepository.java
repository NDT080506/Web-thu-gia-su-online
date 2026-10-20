package org.example.web_thue_gia_su_online.repository.authentication;

import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.authentication.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, String> {

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);
}
