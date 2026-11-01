package org.example.web_thue_gia_su_online.service.article_and_community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.UserRole;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleCommentRepository;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.impl.ArticleServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository repository;

    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleMapper mapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ArticleServiceImpl service;

    private User studentUser;

    @BeforeEach
    void setUp() {
        studentUser = User.builder()
                .id("user-1")
                .displayName("Student One")
                .role(UserRole.STUDENT)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void mockSecurityContext(String userId) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userId);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createArticle_shouldReturnResponse() {
        mockSecurityContext("user-1");
        ArticleCreationRequest request = ArticleCreationRequest.builder()
                .title("New Article")
                .content("Content")
                .build();
        Article article = Article.builder().id("article-1").build();
        ArticleResponse response = ArticleResponse.builder().id("article-1").build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        when(mapper.toArticle(request)).thenReturn(article);
        when(repository.save(any(Article.class))).thenReturn(article);
        when(mapper.toArticleResponse(article)).thenReturn(response);

        ArticleResponse result = service.createArticle(request);

        assertNotNull(result);
        assertEquals("article-1", result.getId());
        verify(repository).save(any(Article.class));
    }

    @Test
    void createArticle_guest_shouldThrowException() {
        mockSecurityContext("guest-1");
        User guestUser = User.builder().id("guest-1").role(UserRole.GUEST).build();
        when(userRepository.findById("guest-1")).thenReturn(Optional.of(guestUser));

        ArticleCreationRequest request = ArticleCreationRequest.builder()
                .title("Guest Article")
                .content("Content")
                .build();

        assertThrows(AppException.class, () -> service.createArticle(request));
    }

    @Test
    void getArticle_shouldReturnResponse() {
        Article article = Article.builder()
                .id("article-1")
                .isPublished(true)
                .minRoleLevel(0)
                .build();
        ArticleResponse response = ArticleResponse.builder().id("article-1").build();

        when(repository.findById("article-1")).thenReturn(Optional.of(article));
        when(mapper.toArticleResponse(article)).thenReturn(response);

        ArticleResponse result = service.getArticle("article-1");

        assertNotNull(result);
        assertEquals("article-1", result.getId());
    }

    @Test
    void getArticle_notFound_shouldThrow() {
        when(repository.findById("article-1")).thenReturn(Optional.empty());
        assertThrows(AppException.class, () -> service.getArticle("article-1"));
    }

    @Test
    void getArticles_shouldReturnList() {
        Article article = Article.builder().id("article-1").build();
        ArticleResponse response = ArticleResponse.builder().id("article-1").build();

        when(repository.findByIsPublishedTrueAndMinRoleLevelLessThanEqualOrderByCreatedAtDesc(anyInt()))
                .thenReturn(List.of(article));
        when(mapper.toArticleResponse(article)).thenReturn(response);

        List<ArticleResponse> result = service.getArticles();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateArticle_shouldReturnResponse() {
        mockSecurityContext("user-1");
        Article article = Article.builder().id("article-1").author(studentUser).build();
        ArticleResponse response = ArticleResponse.builder().id("article-1").build();
        ArticleUpdateRequest request =
                ArticleUpdateRequest.builder().title("Updated").build();

        when(repository.findById("article-1")).thenReturn(Optional.of(article));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        when(repository.save(article)).thenReturn(article);
        when(mapper.toArticleResponse(article)).thenReturn(response);

        ArticleResponse result = service.updateArticle(request, "article-1");

        assertNotNull(result);
        verify(repository).save(article);
    }

    @Test
    void deleteArticle_shouldSucceed() {
        mockSecurityContext("user-1");
        Article article = Article.builder().id("article-1").author(studentUser).build();

        when(repository.findById("article-1")).thenReturn(Optional.of(article));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        doNothing().when(repository).delete(article);

        service.deleteArticle("article-1");

        verify(repository).delete(article);
    }
}
