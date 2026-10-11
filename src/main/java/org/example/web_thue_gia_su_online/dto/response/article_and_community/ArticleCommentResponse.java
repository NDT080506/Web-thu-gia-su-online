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
public class ArticleCommentResponse {

    String id;
    String articleId;
    String userId;
    String content;
    boolean isHidden;
    Instant createdAt;
    Instant updateAt;
}
