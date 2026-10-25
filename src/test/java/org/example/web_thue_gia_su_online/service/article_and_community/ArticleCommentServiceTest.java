package org.example.web_thue_gia_su_online.service.article_and_community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.enums.UserRole;
import org.example.web_thue_gia_su_online.exception.AppException;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleCommentMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleCommentRepository;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.impl.ArticleCommentServiceImpl;
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
class ArticleCommentServiceTest {

    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleCommentMapper articleCommentMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ArticleCommentServiceImpl service;

    private User studentUser;
    private User articleAuthorUser;
    private Article article;

    @BeforeEach
    void setUp() {
        studentUser = User.builder()
                .id("user-1")
                .displayName("Student One")
                .role(UserRole.STUDENT)
                .build();

        articleAuthorUser = User.builder()
                .id("author-1")
                .displayName("Author One")
                .role(UserRole.TUTOR)
                .build();

        article = Article.builder()
                .id("article-1")
                .author(articleAuthorUser)
                .isPublished(true)
                .minRoleLevel(0)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void mockSecurityContext(String userId) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.mockito.Mockito.lenient().when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(userId);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createComment_shouldReturnResponse() {
        mockSecurityContext("user-1");
        ArticleCommentCreationRequest request = ArticleCommentCreationRequest.builder()
                .content("Great article!")
                .build();
        ArticleComment comment = ArticleComment.builder().id("comment-1").build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("comment-1").build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        when(articleRepository.findById("article-1")).thenReturn(Optional.of(article));
        when(articleCommentMapper.toArticleComment(request)).thenReturn(comment);
        when(articleCommentRepository.save(any(ArticleComment.class))).thenReturn(comment);
        when(articleCommentMapper.toArticleCommentResponse(comment)).thenReturn(response);

        ArticleCommentResponse result = service.createComment(request, "article-1");

        assertNotNull(result);
        assertEquals("comment-1", result.getId());
        verify(articleCommentRepository).save(any(ArticleComment.class));
    }

    @Test
    void createComment_guest_shouldThrowException() {
        mockSecurityContext("guest-1");
        User guest = User.builder().id("guest-1").role(UserRole.GUEST).build();
        when(userRepository.findById("guest-1")).thenReturn(Optional.of(guest));

        ArticleCommentCreationRequest request =
                ArticleCommentCreationRequest.builder().content("Hi").build();

        assertThrows(AppException.class, () -> service.createComment(request, "article-1"));
    }

    @Test
    void createComment_articleNotFound_shouldThrow() {
        mockSecurityContext("user-1");
        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        when(articleRepository.findById("article-1")).thenReturn(Optional.empty());

        ArticleCommentCreationRequest request =
                ArticleCommentCreationRequest.builder().content("Hi").build();

        assertThrows(AppException.class, () -> service.createComment(request, "article-1"));
    }

    @Test
    void createComment_withParent_shouldReturnResponse() {
        mockSecurityContext("user-1");
        ArticleComment parent =
                ArticleComment.builder().id("parent-1").article(article).build();
        ArticleCommentCreationRequest request = ArticleCommentCreationRequest.builder()
                .content("Reply to parent")
                .parentId("parent-1")
                .build();
        ArticleComment comment = ArticleComment.builder().id("reply-1").build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("reply-1").build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        when(articleRepository.findById("article-1")).thenReturn(Optional.of(article));
        when(articleCommentRepository.findById("parent-1")).thenReturn(Optional.of(parent));
        when(articleCommentMapper.toArticleComment(request)).thenReturn(comment);
        when(articleCommentRepository.save(any(ArticleComment.class))).thenReturn(comment);
        when(articleCommentMapper.toArticleCommentResponse(comment)).thenReturn(response);

        ArticleCommentResponse result = service.createComment(request, "article-1");

        assertNotNull(result);
        assertEquals("reply-1", result.getId());
    }

    @Test
    void getComment_shouldReturnResponse() {
        ArticleComment comment = ArticleComment.builder()
                .id("1")
                .user(studentUser)
                .isHidden(false)
                .build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        when(articleCommentRepository.findById("1")).thenReturn(Optional.of(comment));
        when(articleCommentMapper.toArticleCommentResponse(comment)).thenReturn(response);

        ArticleCommentResponse result = service.getComment("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getComment_notFound_shouldThrow() {
        when(articleCommentRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(AppException.class, () -> service.getComment("1"));
    }

    @Test
    void getCommentsByArticle_shouldReturnList() {
        ArticleComment comment = ArticleComment.builder()
                .id("1")
                .user(studentUser)
                .isHidden(false)
                .createdAt(Instant.now())
                .build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();

        when(articleRepository.findById("article-1")).thenReturn(Optional.of(article));
        when(articleCommentRepository.findByArticleIdOrderByCreatedAtAsc("article-1"))
                .thenReturn(List.of(comment));
        when(articleCommentMapper.toArticleCommentResponse(comment)).thenReturn(response);

        List<ArticleCommentResponse> result = service.getCommentsByArticle("article-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateComment_asAuthor_shouldReturnResponse() {
        mockSecurityContext("user-1");
        ArticleComment comment = ArticleComment.builder()
                .id("1")
                .article(article)
                .user(studentUser)
                .content("Old content")
                .build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        ArticleCommentUpdateRequest request =
                ArticleCommentUpdateRequest.builder().content("New content").build();

        when(articleCommentRepository.findById("1")).thenReturn(Optional.of(comment));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        when(articleCommentRepository.save(comment)).thenReturn(comment);
        when(articleCommentMapper.toArticleCommentResponse(comment)).thenReturn(response);

        ArticleCommentResponse result = service.updateComment(request, "1");

        assertNotNull(result);
        verify(articleCommentRepository).save(comment);
    }

    @Test
    void updateComment_unauthorized_shouldThrow() {
        mockSecurityContext("other-user");
        User otherUser = User.builder().id("other-user").role(UserRole.STUDENT).build();
        ArticleComment comment = ArticleComment.builder()
                .id("1")
                .article(article)
                .user(studentUser)
                .build();
        ArticleCommentUpdateRequest request =
                ArticleCommentUpdateRequest.builder().content("Hacked content").build();

        when(articleCommentRepository.findById("1")).thenReturn(Optional.of(comment));
        when(userRepository.findById("other-user")).thenReturn(Optional.of(otherUser));

        assertThrows(AppException.class, () -> service.updateComment(request, "1"));
    }

    @Test
    void deleteComment_asAuthor_shouldSucceed() {
        mockSecurityContext("user-1");
        ArticleComment comment = ArticleComment.builder()
                .id("1")
                .article(article)
                .user(studentUser)
                .build();

        when(articleCommentRepository.findById("1")).thenReturn(Optional.of(comment));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        doNothing().when(articleCommentRepository).delete(comment);

        service.deleteComment("1");

        verify(articleCommentRepository).delete(comment);
    }

    @Test
    void deleteComment_asArticleAuthor_shouldSucceed() {
        mockSecurityContext("author-1");
        ArticleComment comment = ArticleComment.builder()
                .id("1")
                .article(article)
                .user(studentUser)
                .build();

        when(articleCommentRepository.findById("1")).thenReturn(Optional.of(comment));
        when(userRepository.findById("author-1")).thenReturn(Optional.of(articleAuthorUser));
        doNothing().when(articleCommentRepository).delete(comment);

        service.deleteComment("1");

        verify(articleCommentRepository).delete(comment);
    }

    @Test
    void deleteComment_unauthorized_shouldThrow() {
        mockSecurityContext("other-user");
        User otherUser = User.builder().id("other-user").role(UserRole.STUDENT).build();
        ArticleComment comment = ArticleComment.builder()
                .id("1")
                .article(article)
                .user(studentUser)
                .build();

        when(articleCommentRepository.findById("1")).thenReturn(Optional.of(comment));
        when(userRepository.findById("other-user")).thenReturn(Optional.of(otherUser));

        assertThrows(AppException.class, () -> service.deleteComment("1"));
    }

    @Test
    void createComment_roleLevelTooLow_shouldThrowException() {
        mockSecurityContext("user-1");
        Article restrictedArticle = Article.builder()
                .id("article-1")
                .author(articleAuthorUser)
                .isPublished(true)
                .minRoleLevel(2) // Requires TUTOR, but studentUser is STUDENT (level 1)
                .build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        when(articleRepository.findById("article-1")).thenReturn(Optional.of(restrictedArticle));

        ArticleCommentCreationRequest request = ArticleCommentCreationRequest.builder()
                .content("Cannot comment here")
                .build();

        assertThrows(AppException.class, () -> service.createComment(request, "article-1"));
    }

    @Test
    void createComment_parentBelongsToDifferentArticle_shouldThrowException() {
        mockSecurityContext("user-1");
        Article otherArticle = Article.builder().id("article-99").build();
        ArticleComment parentOfOtherArticle =
                ArticleComment.builder().id("parent-diff").article(otherArticle).build();

        when(userRepository.findById("user-1")).thenReturn(Optional.of(studentUser));
        when(articleRepository.findById("article-1")).thenReturn(Optional.of(article));
        when(articleCommentRepository.findById("parent-diff")).thenReturn(Optional.of(parentOfOtherArticle));

        ArticleCommentCreationRequest request = ArticleCommentCreationRequest.builder()
                .content("Reply to wrong parent")
                .parentId("parent-diff")
                .build();

        assertThrows(AppException.class, () -> service.createComment(request, "article-1"));
    }

    @Test
    void getComment_hiddenComment_normalUser_shouldThrow() {
        mockSecurityContext("other-user");
        ArticleComment hiddenComment = ArticleComment.builder()
                .id("hidden-1")
                .user(studentUser)
                .isHidden(true)
                .build();

        User normalUser = User.builder().id("other-user").role(UserRole.STUDENT).build();
        when(articleCommentRepository.findById("hidden-1")).thenReturn(Optional.of(hiddenComment));
        when(userRepository.findById("other-user")).thenReturn(Optional.of(normalUser));

        assertThrows(AppException.class, () -> service.getComment("hidden-1"));
    }

    @Test
    void getCommentsByArticle_adminCanSeeHiddenComments() {
        mockSecurityContext("admin-1");
        User adminUser = User.builder().id("admin-1").role(UserRole.ADMIN).build();
        ArticleComment hiddenComment = ArticleComment.builder()
                .id("c-hidden")
                .user(studentUser)
                .isHidden(true)
                .createdAt(Instant.now())
                .build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("c-hidden").isHidden(true).build();

        when(userRepository.findById("admin-1")).thenReturn(Optional.of(adminUser));
        when(articleRepository.findById("article-1")).thenReturn(Optional.of(article));
        when(articleCommentRepository.findByArticleIdOrderByCreatedAtAsc("article-1"))
                .thenReturn(List.of(hiddenComment));
        when(articleCommentMapper.toArticleCommentResponse(hiddenComment)).thenReturn(response);

        List<ArticleCommentResponse> result = service.getCommentsByArticle("article-1");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("c-hidden", result.get(0).getId());
    }
}
