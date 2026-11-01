package org.example.web_thue_gia_su_online.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.resend.Resend;

@Configuration
public class ResendConfig {

    @Bean
    public Resend resend(@Value("${app.mail.resend-api-key}") String apiKey) {
        return new Resend(apiKey);
    }
}
