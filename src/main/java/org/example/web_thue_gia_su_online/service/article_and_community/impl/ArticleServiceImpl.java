package org.example.web_thue_gia_su_online.service.article_and_community.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.ActionCode;
import org.example.web_thue_gia_su_online.enums.identity_and_administration.UserRole;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleCommentRepository;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleService;
import org.example.web_thue_gia_su_online.service.identity_and_administration.ActionLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
    ActionLogService actionLogService;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR', 'ADMIN')")
    public ArticleResponse createArticle(ArticleCreationRequest request) {
        String currentUserId = getCurrentUserId();
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

        Article savedArticle = articleRepository.save(article);
        actionLogService.log(ActionCode.CREATE_ARTICLE, author, "Article", savedArticle.getId());
        return articleMapper.toArticleResponse(savedArticle);
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
    public Page<ArticleResponse> getArticles(Pageable pageable) {
        int currentRoleLevel = getCurrentUserRoleLevel();
        return articleRepository
                .findByIsPublishedTrueAndMinRoleLevelLessThanEqualOrderByCreatedAtDesc(currentRoleLevel, pageable)
                .map(articleMapper::toArticleResponse);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public Page<ArticleResponse> getMyArticles(Pageable pageable) {
        String currentUserId = getCurrentUserId();
        return articleRepository
                .findByAuthorIdOrderByCreatedAtDesc(currentUserId, pageable)
                .map(articleMapper::toArticleResponse);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ArticleResponse updateArticle(ArticleUpdateRequest request, String id) {
        Article article =
                articleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        String currentUserId = getCurrentUserId();
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
        articleRepository.save(article);
        actionLogService.log(ActionCode.UPDATE_ARTICLE, currentUser, "Article", id);
        return articleMapper.toArticleResponse(article);
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
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

        List<ArticleComment> comments =
                articleCommentRepository.findByArticleId(id, Pageable.unpaged()).getContent();
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
        actionLogService.log(ActionCode.DELETE_ARTICLE, currentUser, "Article", id);
    }

    private int getCurrentUserRoleLevel() {

        String userId = getCurrentUserIdOrNull();
        if (userId == null) {
            return 0;
        }
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
