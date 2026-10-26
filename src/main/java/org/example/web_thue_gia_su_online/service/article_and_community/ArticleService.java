package org.example.web_thue_gia_su_online.service.article_and_community;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;

public interface ArticleService {

    ArticleResponse createArticle(ArticleCreationRequest request);

    ArticleResponse getArticle(String id);

    List<ArticleResponse> getArticles();

    List<ArticleResponse> getMyArticles();

    ArticleResponse updateArticle(ArticleUpdateRequest request, String id);

    void deleteArticle(String id);
}
