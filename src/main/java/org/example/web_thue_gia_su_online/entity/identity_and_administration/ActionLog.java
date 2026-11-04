package org.example.web_thue_gia_su_online.entity.identity_and_administration;

import java.time.Instant;

import jakarta.persistence.*;

import org.example.web_thue_gia_su_online.enums.ActionCode;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        name = "action_logs",
        indexes = {
            @Index(name = "idx_action_log_user", columnList = "user_id"),
            @Index(name = "idx_action_log_created_at", columnList = "created_at"),
            @Index(name = "idx_action_log_target", columnList = "target_type, target_id")
        })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    ActionCode action;

    @Column(name = "target_type", length = 50)
    String targetType;

    @Column(name = "target_id")
    String targetId;

    String ipAddress;

    @Column(columnDefinition = "TEXT")
    String userAgent;

    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}
