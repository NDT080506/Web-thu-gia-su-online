package org.example.web_thue_gia_su_online.service.article_and_community;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleCommentService {

    ArticleCommentResponse createComment(ArticleCommentCreationRequest request, String articleId);

    ArticleCommentResponse getComment(String id);

    Page<ArticleCommentResponse> getCommentsByArticle(String articleId, Pageable pageable);

    ArticleCommentResponse updateComment(ArticleCommentUpdateRequest request, String id);

    void deleteComment(String id);
}
