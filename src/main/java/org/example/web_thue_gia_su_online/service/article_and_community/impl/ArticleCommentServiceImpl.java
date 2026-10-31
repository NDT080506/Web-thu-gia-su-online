package org.example.web_thue_gia_su_online.service.article_and_community.impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.UserRole;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.exception.ErrorCode;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleCommentMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleCommentRepository;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleCommentService;
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
public class ArticleCommentServiceImpl implements ArticleCommentService {

    ArticleCommentRepository articleCommentRepository;
    ArticleRepository articleRepository;
    UserRepository userRepository;
    ArticleCommentMapper articleCommentMapper;

    private String getCurrentUserId() {
        return Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getName();
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR', 'ADMIN')")
    public ArticleCommentResponse createComment(ArticleCommentCreationRequest request, String articleId) {
        String currentUserId = getCurrentUserId();
        return createCommentInternal(request, articleId, currentUserId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR', 'ADMIN')")
    public ArticleCommentResponse createComment(
            ArticleCommentCreationRequest request, String articleId, String userId) {
        String effectiveUserId = (userId != null && !userId.isBlank()) ? userId : getCurrentUserId();
        return createCommentInternal(request, articleId, effectiveUserId);
    }

    private ArticleCommentResponse createCommentInternal(
            ArticleCommentCreationRequest request, String articleId, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getRole() == UserRole.GUEST) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Article article =
                articleRepository.findById(articleId).orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));

        if (!article.isPublished()) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        int userRoleLevel =
                switch (user.getRole()) {
                    case ADMIN -> 3;
                    case TUTOR -> 2;
                    case STUDENT -> 1;
                    case GUEST -> 0;
                };

        if (article.getMinRoleLevel() > userRoleLevel) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        ArticleComment parent = null;
        if (request.getParentId() != null && !request.getParentId().isBlank()) {
            parent = articleCommentRepository
                    .findById(request.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
            if (!parent.getArticle().getId().equals(articleId)) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }
        }

        ArticleComment comment = articleCommentMapper.toArticleComment(request);
        comment.setArticle(article);
        comment.setUser(user);
        comment.setParent(parent);
        comment.setHidden(false);
        comment.setCreatedAt(Instant.now());

        return articleCommentMapper.toArticleCommentResponse(articleCommentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleCommentResponse getComment(String id) {
        ArticleComment comment =
                articleCommentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (comment.isHidden()) {
            int currentRoleLevel = getCurrentUserRoleLevel();
            String currentUserId = getCurrentUserIdOrNull();
            boolean isAuthor = currentUserId != null
                    && currentUserId.equals(comment.getUser().getId());
            boolean isAdmin = currentRoleLevel >= 3;
            if (!isAuthor && !isAdmin) {
                throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
            }
        }

        return articleCommentMapper.toArticleCommentResponse(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleCommentResponse> getCommentsByArticle(String articleId) {
        Article article =
                articleRepository.findById(articleId).orElseThrow(() -> new AppException(ErrorCode.ARTICLE_NOT_FOUND));

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

        List<ArticleComment> comments = articleCommentRepository.findByArticleIdOrderByCreatedAtAsc(articleId);
        String currentUserId = getCurrentUserIdOrNull();
        boolean isAdmin = currentRoleLevel >= 3;

        return comments.stream()
                .filter(c -> !c.isHidden()
                        || isAdmin
                        || (currentUserId != null
                                && currentUserId.equals(c.getUser().getId())))
                .map(articleCommentMapper::toArticleCommentResponse)
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ArticleCommentResponse updateComment(ArticleCommentUpdateRequest request, String id) {
        ArticleComment comment =
                articleCommentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        String currentUserId = getCurrentUserId();
        User currentUser =
                userRepository.findById(currentUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isAuthor = comment.getUser().getId().equals(currentUserId);
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
        boolean isArticleAuthor = comment.getArticle().getAuthor().getId().equals(currentUserId);

        if (!isAuthor && !isAdmin && !isArticleAuthor) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (isAuthor && !isAdmin && !isArticleAuthor) {
            if (request.getContent() != null && !request.getContent().isBlank()) {
                comment.setContent(request.getContent());
            }
        } else {
            articleCommentMapper.updateArticleComment(comment, request);
        }

        return articleCommentMapper.toArticleCommentResponse(articleCommentRepository.save(comment));
    }

    @Override
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void deleteComment(String id) {
        ArticleComment comment =
                articleCommentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        String currentUserId = getCurrentUserId();
        User currentUser =
                userRepository.findById(currentUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isCommentAuthor = comment.getUser().getId().equals(currentUserId);
        boolean isArticleAuthor = comment.getArticle().getAuthor().getId().equals(currentUserId);
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isCommentAuthor && !isArticleAuthor && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        articleCommentRepository.delete(comment);
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
