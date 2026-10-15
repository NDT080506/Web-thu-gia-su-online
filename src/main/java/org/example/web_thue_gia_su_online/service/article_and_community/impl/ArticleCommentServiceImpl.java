package org.example.web_thue_gia_su_online.service.article_and_community.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleCommentMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleCommentRepository;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleCommentService;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleCommentServiceImpl implements ArticleCommentService {

    ArticleCommentRepository articleCommentRepository;
    ArticleRepository articleRepository;
    UserRepository userRepository;
    ArticleCommentMapper articleCommentMapper;

    @Override
    public ArticleCommentResponse createComment(
            ArticleCommentCreationRequest request, String articleId, String userId) {
        Article article =
                articleRepository.findById(articleId).orElseThrow(() -> new RuntimeException("Article not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        ArticleComment parent = null;
        if (request.getParentId() != null && !request.getParentId().isBlank()) {
            parent = articleCommentRepository
                    .findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }

        ArticleComment comment = articleCommentMapper.toArticleComment(request);
        comment.setArticle(article);
        comment.setUser(user);
        comment.setParent(parent);
        comment.setCreatedAt(Instant.now());

        return articleCommentMapper.toArticleCommentResponse(articleCommentRepository.save(comment));
    }

    @Override
    public ArticleCommentResponse getComment(String id) {
        ArticleComment comment =
                articleCommentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        return articleCommentMapper.toArticleCommentResponse(comment);
    }

    @Override
    public List<ArticleCommentResponse> getCommentsByArticle(String articleId) {
        return articleCommentRepository.findByArticleId(articleId).stream()
                .map(articleCommentMapper::toArticleCommentResponse)
                .toList();
    }

    @Override
    public ArticleCommentResponse updateComment(ArticleCommentUpdateRequest request, String id) {
        ArticleComment comment =
                articleCommentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        articleCommentMapper.updateArticleComment(comment, request);

        return articleCommentMapper.toArticleCommentResponse(articleCommentRepository.save(comment));
    }

    @Override
    public void deleteComment(String id) {
        ArticleComment comment =
                articleCommentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        articleCommentRepository.delete(comment);
    }
}