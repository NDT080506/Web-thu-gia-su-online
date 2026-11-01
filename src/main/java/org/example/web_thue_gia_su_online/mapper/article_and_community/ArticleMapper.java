package org.example.web_thue_gia_su_online.mapper.article_and_community;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    Article toArticle(ArticleCreationRequest request);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "published", target = "isPublished")
    ArticleResponse toArticleResponse(Article article);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "isPublished", target = "published")
    void updateArticle(@MappingTarget Article article, ArticleUpdateRequest request);
}
