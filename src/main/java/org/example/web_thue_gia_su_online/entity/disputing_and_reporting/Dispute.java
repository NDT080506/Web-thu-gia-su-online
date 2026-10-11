package org.example.web_thue_gia_su_online.entity.disputing_and_reporting;

import java.time.Instant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Session;
import org.example.web_thue_gia_su_online.enums.DisputeStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Dispute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raised_by", nullable = false)
    User raisedBy;

    @NotNull
    String reasonType;

    String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    DisputeStatus status = DisputeStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    User admin;

    String resolutionNote;

    @NotNull
    Instant createdAt;

    @NonNull
    Instant updateAt;
}
