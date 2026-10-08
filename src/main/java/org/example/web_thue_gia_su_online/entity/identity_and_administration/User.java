package org.example.web_thue_gia_su_online.entity.identity_and_administration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.learning_and_education.Order;
import org.example.web_thue_gia_su_online.entity.learning_and_education.OrderMessage;
import org.example.web_thue_gia_su_online.entity.tutoring.TutorApplication;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NonNull
    String email;

    @NonNull
    String passwordHash;

    @NotNull
    String display_name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @NonNull
    UserRole role = UserRole.MEMBER;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @NotNull
    AccountStatus status = AccountStatus.ACTIVE;

    String phone;
    String zaloLink;

    @NotNull
    Instant createdAt;

    @NonNull
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
    List<OrderMessage> orderMessageList = new ArrayList<>();
}
