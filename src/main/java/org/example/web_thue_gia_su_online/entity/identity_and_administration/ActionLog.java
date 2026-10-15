package org.example.web_thue_gia_su_online.entity.identity_and_administration;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        name = "action_logs",
        indexes = {
            @Index(columnList = "target_user_id"),
            @Index(columnList = "target_id"),
            @Index(columnList = "created_at")
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
    @JoinColumn(name = "admin_id")
    User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id")
    User targetUser;

    @Column(length = 50)
    String targetType;

    UUID targetId;

    @Column(length = 100)
    String actionCode;

    @Column(columnDefinition = "jsonb")
    String oldValue;

    @Column(columnDefinition = "jsonb")
    String newValue;

    @Column(columnDefinition = "TEXT")
    String reason;

    @NotNull
    Instant createdAt;
}
