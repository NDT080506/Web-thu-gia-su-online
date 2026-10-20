package org.example.web_thue_gia_su_online.entity.authentication;

import java.time.Instant;

import jakarta.persistence.*;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    String tokenHash;

    @Column(name = "expires_at", nullable = false)
    Instant expiresAt;

    @Column(name = "used_at")
    Instant usedAt;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    Instant createdAt = Instant.now();
}
