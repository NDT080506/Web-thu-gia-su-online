package org.example.web_thue_gia_su_online.service.article_and_community;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;

public interface ArticleCommentService {

    ArticleCommentResponse createComment(ArticleCommentCreationRequest request, String articleId);

    ArticleCommentResponse createComment(ArticleCommentCreationRequest request, String articleId, String userId);

    ArticleCommentResponse getComment(String id);

    List<ArticleCommentResponse> getCommentsByArticle(String articleId);

    ArticleCommentResponse updateComment(ArticleCommentUpdateRequest request, String id);

    void deleteComment(String id);
}
