package org.example.web_thue_gia_su_online.dto.request.article_and_community;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleUpdateRequest {

    String title;
    String content;
    Boolean isPublished;
}