package org.example.web_thue_gia_su_online.entity.article_and_community;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "articles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    User author;

    @Column(length = 150)
    String authorNickname;

    @Column(length = 500)
    String authorAvatarUrl;

    @NotNull
    @Column(length = 255, nullable = false)
    String title;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    @Builder.Default
    int minRoleLevel = 0;

    @Builder.Default
    boolean isPublished = true;

    @NotNull
    Instant createdAt;

    Instant updatedAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ArticleComment> comments = new ArrayList<>();
}
