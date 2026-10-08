package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.OrderStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    User student;

    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    User tutor;

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    ServicePackage servicePackage;

    @NotNull
    @Builder.Default
    OrderStatus status = OrderStatus.PENDING;

    @NotNull
    long total_price;

    int sessions_remaining;

    @NotNull
    Instant created_at;

    @NonNull
    Instant updateAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderMessage> orderMessageList = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Session> sessions = new ArrayList<>();
}
