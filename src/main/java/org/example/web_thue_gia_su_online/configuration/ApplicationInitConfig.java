package org.example.web_thue_gia_su_online.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.UserRole;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByDisplayName("admin")) {

                User user = User.builder()
                        .displayName("admin")
                        .passwordHash(passwordEncoder.encode("123456789"))
                        .role(UserRole.ADMIN)
                        .email("admin123@gmail.com")
                        .emailVerifiedAt(Instant.now())
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it !");
            }
        };
    }
}
