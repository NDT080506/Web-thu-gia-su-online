package org.example.web_thue_gia_su_online.mapper.article_and_community;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ArticleCommentMapper {

    ArticleComment toArticleComment(ArticleCommentCreationRequest request);

    @Mapping(source = "article.id", target = "articleId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.displayName", target = "userDisplayName")
    @Mapping(source = "user.avatarUrl", target = "userAvatarUrl")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "hidden", target = "isHidden")
    ArticleCommentResponse toArticleCommentResponse(ArticleComment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "isHidden", target = "hidden")
    void updateArticleComment(@MappingTarget ArticleComment comment, ArticleCommentUpdateRequest request);
}
