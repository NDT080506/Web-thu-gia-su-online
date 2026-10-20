package org.example.web_thue_gia_su_online.service.authentication;

public interface EmailService {

    void sendVerificationEmail(String email, String token);
}
