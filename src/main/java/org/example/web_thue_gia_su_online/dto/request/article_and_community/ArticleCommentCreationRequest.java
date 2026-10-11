package org.example.web_thue_gia_su_online.dto.request.article_and_community;

import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleCommentCreationRequest {

    @NotBlank(message = "Comment content must not be blank")
    String content;
}
