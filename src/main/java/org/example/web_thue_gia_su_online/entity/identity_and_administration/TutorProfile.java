package org.example.web_thue_gia_su_online.entity.identity_and_administration;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorProfile {

    @Id
    String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    User user;

    String bio;
    String subjectCanTeach;

    @Builder.Default
    double rating = 5.0;

    @NonNull
    Instant createdAt;

    @NonNull
    Instant updateAt;
}
