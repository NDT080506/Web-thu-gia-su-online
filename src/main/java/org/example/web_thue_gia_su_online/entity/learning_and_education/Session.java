package org.example.web_thue_gia_su_online.entity.learning_and_education;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    Instant start_time;

    Instant end_time;

    String meet_url;

    @Builder.Default
    boolean is_completed = false;

    Instant created_at;

    Instant updateAt;
}
