package org.example.web_thue_gia_su_online.controller.article_and_community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleCommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ArticleCommentControllerTest {

    @Mock
    private ArticleCommentService articleCommentService;

    @InjectMocks
    private ArticleCommentController articleCommentController;

    @Test
    void createComment_withArticleId_shouldReturnCommentResponse() {
        ArticleCommentCreationRequest request =
                ArticleCommentCreationRequest.builder().content("Test comment").build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        when(articleCommentService.createComment(eq(request), eq("article-1"))).thenReturn(response);

        ApiResponse<ArticleCommentResponse> apiResponse = articleCommentController.createComment("article-1", request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        assertNotNull(apiResponse.getResult());
        verify(articleCommentService).createComment(eq(request), eq("article-1"));
    }

    @Test
    void getComment_shouldReturnCommentResponse() {
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        when(articleCommentService.getComment("1")).thenReturn(response);

        ApiResponse<ArticleCommentResponse> apiResponse = articleCommentController.getComment("1");

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(articleCommentService).getComment("1");
    }

    @Test
    void getCommentsByArticle_shouldReturnListOfComments() {
        List<ArticleCommentResponse> list =
                List.of(ArticleCommentResponse.builder().id("1").build());
        Page<ArticleCommentResponse> page = new PageImpl<>(list);
        when(articleCommentService.getCommentsByArticle(eq("article-1"), (Pageable) org.mockito.ArgumentMatchers.any()))
                .thenReturn(page);

        ApiResponse<Page<ArticleCommentResponse>> apiResponse =
                articleCommentController.getCommentsByArticle("article-1", Pageable.unpaged());

        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().getSize());
        verify(articleCommentService)
                .getCommentsByArticle(eq("article-1"), (Pageable) org.mockito.ArgumentMatchers.any());
    }

    @Test
    void updateComment_shouldReturnUpdatedCommentResponse() {
        ArticleCommentUpdateRequest request =
                ArticleCommentUpdateRequest.builder().content("Updated comment").build();
        ArticleCommentResponse response =
                ArticleCommentResponse.builder().id("1").build();
        when(articleCommentService.updateComment(eq(request), eq("1"))).thenReturn(response);

        ApiResponse<ArticleCommentResponse> apiResponse = articleCommentController.updateComment("1", request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(articleCommentService).updateComment(eq(request), eq("1"));
    }

    @Test
    void deleteComment_shouldReturnSuccessMessage() {
        doNothing().when(articleCommentService).deleteComment("1");

        ApiResponse<Void> apiResponse = articleCommentController.deleteComment("1");

        assertNotNull(apiResponse);
        assertEquals("Comment has been deleted", apiResponse.getMessage());
        verify(articleCommentService).deleteComment("1");
    }
}
