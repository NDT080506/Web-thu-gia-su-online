package org.example.web_thue_gia_su_online.configuration;

import java.time.Instant;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Wallet;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.finance_and_wallet.WalletType;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.UserRole;
import org.example.web_thue_gia_su_online.repository.finance_and_wallet.WalletRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, WalletRepository walletRepository) {
        return args -> {
            User admin = userRepository
                    .findByEmail("admin123@gmail.com")
                    .or(() -> userRepository.findByEmail("admin@gmail.com"))
                    .orElseGet(() -> {
                        User user = User.builder()
                                .displayName("admin")
                                .passwordHash(passwordEncoder.encode("123456789"))
                                .role(UserRole.ADMIN)
                                .email("admin123@gmail.com")
                                .emailVerifiedAt(Instant.now())
                                .build();
                        log.warn("admin user has been created with default password: admin, please change it !");
                        return userRepository.save(user);
                    });

            if (!walletRepository.existsByType(WalletType.PLATFORM)) {
                Wallet platformWallet = Wallet.builder()
                        .user(admin)
                        .type(WalletType.PLATFORM)
                        .availableBalance(0)
                        .holdingBalance(0)
                        .createdAt(Instant.now())
                        .build();
                walletRepository.save(platformWallet);
                log.info("Initialized PLATFORM wallet for admin");
            }
        };
    }
}
