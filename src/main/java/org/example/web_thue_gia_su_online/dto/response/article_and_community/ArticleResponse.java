package org.example.web_thue_gia_su_online.dto.response.article_and_community;

import java.time.Instant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleResponse {

    String id;
    String authorId;
    String authorNickname;
    String authorAvatarUrl;
    String title;
    String content;
    int minRoleLevel;
    boolean isPublished;
    Instant createdAt;
    Instant updatedAt;
}
