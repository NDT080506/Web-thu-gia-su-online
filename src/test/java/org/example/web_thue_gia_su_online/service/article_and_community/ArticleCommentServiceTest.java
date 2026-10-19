package org.example.web_thue_gia_su_online.service.article_and_community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleCommentMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleCommentRepository;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.impl.ArticleCommentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @Mock
    private ArticleCommentRepository repository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleCommentMapper mapper;

    @InjectMocks
    private ArticleCommentServiceImpl service;

    @Test
    void createComment_shouldReturnResponse() {
        ArticleComment comment = ArticleComment.builder().id("1").build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        when(articleRepository.findById(any())).thenReturn(Optional.of(any()));
        when(userRepository.findById(any())).thenReturn(Optional.of(any()));
        when(mapper.toArticleComment(any())).thenReturn(comment);
        when(repository.save(any())).thenReturn(comment);
        when(mapper.toArticleCommentResponse(any())).thenReturn(response);

        ArticleCommentResponse result = service.createComment(any(), "article-1", "user-1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getComment_shouldReturnResponse() {
        ArticleComment comment = ArticleComment.builder().id("1").build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(comment));
        when(mapper.toArticleCommentResponse(any())).thenReturn(response);

        ArticleCommentResponse result = service.getComment("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getComment_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getComment("1"));
    }

    @Test
    void getCommentsByArticle_shouldReturnList() {
        ArticleComment comment = ArticleComment.builder().id("1").build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        when(repository.findByArticleId("article-1")).thenReturn(List.of(comment));
        when(mapper.toArticleCommentResponse(any())).thenReturn(response);

        List<ArticleCommentResponse> result = service.getCommentsByArticle("article-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateComment_shouldReturnResponse() {
        ArticleComment comment = ArticleComment.builder().id("1").build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(comment));
        when(repository.save(any())).thenReturn(comment);
        when(mapper.toArticleCommentResponse(any())).thenReturn(response);

        ArticleCommentResponse result = service.updateComment(any(), "1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void deleteComment_shouldSucceed() {
        ArticleComment comment = ArticleComment.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(comment));
        doNothing().when(repository).delete(comment);

        service.deleteComment("1");

        verify(repository).delete(comment);
    }
}
