package org.example.web_thue_gia_su_online.entity.identity_and_administration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import org.example.web_thue_gia_su_online.entity.authentication.InvalidatedToken;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.entity.learning_and_education.OrderMessage;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorProfile;
import org.example.web_thue_gia_su_online.enums.AccountStatus;
import org.example.web_thue_gia_su_online.enums.UserRole;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String email;

    String passwordHash;

    @Column(name = "display_name")
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    UserRole role = UserRole.MEMBER;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    AccountStatus status = AccountStatus.ACTIVE;

    String phone;
    String zaloLink;

    Instant createdAt;

    Instant updateAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    TutorProfile tutorProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<TutorApplication> tutorApplications = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Order> studentOrders = new ArrayList<>();

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Order> tutorOrders = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<OrderMessage> orderMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<InvalidatedToken> invalidatedTokens = new ArrayList<>();
}
