package org.example.web_thue_gia_su_online.service.article_and_community;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    ArticleResponse createArticle(ArticleCreationRequest request);

    ArticleResponse getArticle(String id);

    Page<ArticleResponse> getArticles(Pageable pageable);

    Page<ArticleResponse> getMyArticles(Pageable pageable);

    ArticleResponse updateArticle(ArticleUpdateRequest request, String id);

    void deleteArticle(String id);
}
