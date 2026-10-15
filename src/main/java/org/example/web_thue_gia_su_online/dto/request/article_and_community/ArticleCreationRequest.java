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
public class ArticleCreationRequest {

    @NotBlank(message = "Title must not be blank")
    String title;

    @NotBlank(message = "Content must not be blank")
    String content;

    String authorNickname;
    String authorAvatarUrl;
    Integer minRoleLevel;
}
