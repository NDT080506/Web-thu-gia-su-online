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

import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.impl.ArticleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleMapper mapper;

    @InjectMocks
    private ArticleServiceImpl service;

    @Test
    void createArticle_shouldReturnResponse() {
        Article article = Article.builder().id("1").build();
        ArticleResponse response = ArticleResponse.builder().id("1").build();
        when(userRepository.findById(any())).thenReturn(Optional.of(any()));
        when(mapper.toArticle(any())).thenReturn(article);
        when(repository.save(any())).thenReturn(article);
        when(mapper.toArticleResponse(any())).thenReturn(response);

        ArticleResponse result = service.createArticle(any(), "author-1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getArticle_shouldReturnResponse() {
        Article article = Article.builder().id("1").build();
        ArticleResponse response = ArticleResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(article));
        when(mapper.toArticleResponse(any())).thenReturn(response);

        ArticleResponse result = service.getArticle("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getArticle_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getArticle("1"));
    }

    @Test
    void getArticles_shouldReturnList() {
        Article article = Article.builder().id("1").build();
        ArticleResponse response = ArticleResponse.builder().id("1").build();
        when(repository.findAll()).thenReturn(List.of(article));
        when(mapper.toArticleResponse(any())).thenReturn(response);

        List<ArticleResponse> result = service.getArticles();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateArticle_shouldReturnResponse() {
        Article article = Article.builder().id("1").build();
        ArticleResponse response = ArticleResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(article));
        when(repository.save(any())).thenReturn(article);
        when(mapper.toArticleResponse(any())).thenReturn(response);

        ArticleResponse result = service.updateArticle(any(), "1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void deleteArticle_shouldSucceed() {
        Article article = Article.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(article));
        doNothing().when(repository).delete(article);

        service.deleteArticle("1");

        verify(repository).delete(article);
    }
}
