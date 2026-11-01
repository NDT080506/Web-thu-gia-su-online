package org.example.web_thue_gia_su_online.controller.article_and_community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    @Test
    void createArticle_shouldReturnArticleResponse() {
        ArticleCreationRequest request = ArticleCreationRequest.builder()
                .title("Test Article")
                .content("Test content")
                .build();
        ArticleResponse response = ArticleResponse.builder().id("1").build();
        when(articleService.createArticle(eq(request))).thenReturn(response);

        ApiResponse<ArticleResponse> apiResponse = articleController.createArticle(request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(articleService).createArticle(eq(request));
    }

    @Test
    void getArticle_shouldReturnArticleResponse() {
        ArticleResponse response = ArticleResponse.builder().id("1").build();
        when(articleService.getArticle("1")).thenReturn(response);

        ApiResponse<ArticleResponse> apiResponse = articleController.getArticle("1");

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(articleService).getArticle("1");
    }

    @Test
    void getArticles_shouldReturnListOfArticles() {
        List<ArticleResponse> list = List.of(ArticleResponse.builder().id("1").build());
        when(articleService.getArticles()).thenReturn(list);

        ApiResponse<List<ArticleResponse>> apiResponse = articleController.getArticles();

        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
        verify(articleService).getArticles();
    }

    @Test
    void updateArticle_shouldReturnUpdatedArticleResponse() {
        ArticleUpdateRequest request =
                ArticleUpdateRequest.builder().title("Updated Article").build();
        ArticleResponse response = ArticleResponse.builder().id("1").build();
        when(articleService.updateArticle(eq(request), eq("1"))).thenReturn(response);

        ApiResponse<ArticleResponse> apiResponse = articleController.updateArticle("1", request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(articleService).updateArticle(eq(request), eq("1"));
    }

    @Test
    void deleteArticle_shouldReturnSuccessMessage() {
        doNothing().when(articleService).deleteArticle("1");

        ApiResponse<Void> apiResponse = articleController.deleteArticle("1");

        assertNotNull(apiResponse);
        assertEquals("Article has been deleted", apiResponse.getMessage());
        verify(articleService).deleteArticle("1");
    }
}
