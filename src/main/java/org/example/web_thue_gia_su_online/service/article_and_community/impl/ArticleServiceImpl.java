package org.example.web_thue_gia_su_online.service.article_and_community.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.UserRole;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleCommentRepository;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArticleServiceImpl implements ArticleService {

    ArticleRepository articleRepository;
    ArticleCommentRepository articleCommentRepository;
    UserRepository userRepository;
    ArticleMapper articleMapper;

    @Override
    @Transactional
    public ArticleResponse createArticle(ArticleCreationRequest request) {
        String currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();
        User author =
                userRepository.findById(currentUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (author.getRole() == UserRole.GUEST) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Article article = articleMapper.toArticle(request);
        article.setAuthor(author);
        article.setAuthorNickname(
                (request.getAuthorNickname() != null
                                && !request.getAuthorNickname().isBlank())
                        ? request.getAuthorNickname()
                        : author.getDisplayName());
        article.setAuthorAvatarUrl(
                (request.getAuthorAvatarUrl() != null
                                && !request.getAuthorAvatarUrl().isBlank())
                        ? request.getAuthorAvatarUrl()
                        : author.getAvatarUrl());

        if (request.getMinRoleLevel() == null) {
            article.setMinRoleLevel(0);
        } else {
            article.setMinRoleLevel(request.getMinRoleLevel());
        }

        article.setPublished(true);
        Instant now = Instant.now();
        article.setCreatedAt(now);
        article.setUpdatedAt(now);

        return articleMapper.toArticleResponse(articleRepository.save(article));
    }

    @Override
    public ArticleResponse getArticle(String id) {
        Article article =
                articleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        int currentRoleLevel = getCurrentUserRoleLevel();

        if (!article.isPublished()) {
            String currentUserId = getCurrentUserIdOrNull();
            boolean isAuthor = currentUserId != null
                    && currentUserId.equals(article.getAuthor().getId());
            boolean isAdmin = currentRoleLevel >= 3;
            if (!isAuthor && !isAdmin) {
                throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
            }
        }

        if (article.getMinRoleLevel() > currentRoleLevel) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return articleMapper.toArticleResponse(article);
    }

    @Override
    public List<ArticleResponse> getArticles() {
        int currentRoleLevel = getCurrentUserRoleLevel();
        return articleRepository
                .findByIsPublishedTrueAndMinRoleLevelLessThanEqualOrderByCreatedAtDesc(currentRoleLevel)
                .stream()
                .map(articleMapper::toArticleResponse)
                .toList();
    }

    @Override
    public List<ArticleResponse> getMyArticles() {
        String currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();
        return articleRepository.findByAuthorIdOrderByCreatedAtDesc(currentUserId).stream()
                .map(articleMapper::toArticleResponse)
                .toList();
    }

    @Override
    @Transactional
    public ArticleResponse updateArticle(ArticleUpdateRequest request, String id) {
        Article article =
                articleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        String currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser =
                userRepository.findById(currentUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isAuthor =
                article.getAuthor() != null && article.getAuthor().getId().equals(currentUserId);
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        articleMapper.updateArticle(article, request);
        article.setUpdatedAt(Instant.now());

        return articleMapper.toArticleResponse(articleRepository.save(article));
    }

    @Override
    @Transactional
    public void deleteArticle(String id) {
        Article article =
                articleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        String currentUserId =
                SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser =
                userRepository.findById(currentUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isAuthor =
                article.getAuthor() != null && article.getAuthor().getId().equals(currentUserId);
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        List<ArticleComment> comments = articleCommentRepository.findByArticleId(id);
        if (comments != null && !comments.isEmpty()) {
            List<ArticleComment> replies =
                    comments.stream().filter(c -> c.getParent() != null).toList();
            if (!replies.isEmpty()) {
                articleCommentRepository.deleteAll(replies);
            }
            List<ArticleComment> rootComments =
                    comments.stream().filter(c -> c.getParent() == null).toList();
            if (!rootComments.isEmpty()) {
                articleCommentRepository.deleteAll(rootComments);
            }
        }

        articleRepository.delete(article);
    }

    private int getCurrentUserRoleLevel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return 0;
        }
        String userId = auth.getName();
        return userRepository
                .findById(userId)
                .map(user -> switch (user.getRole()) {
                    case ADMIN -> 3;
                    case TUTOR -> 2;
                    case STUDENT -> 1;
                    case GUEST -> 0;
                })
                .orElse(0);
    }

    private String getCurrentUserIdOrNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return auth.getName();
    }
}
