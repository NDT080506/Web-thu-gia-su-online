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

    @NotNull
    String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    String content;

    @Builder.Default
    boolean isPublished = true;

    @NotNull
    Instant createdAt;

    @NonNull
    Instant updateAt;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ArticleComment> articleComments = new ArrayList<>();
}
