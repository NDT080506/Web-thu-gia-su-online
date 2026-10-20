package org.example.web_thue_gia_su_online.service.authentication;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

public interface EmailVerificationService {
    void sendVerificationEmail(User user);

    void verifyEmail(String token);
}
