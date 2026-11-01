package org.example.web_thue_gia_su_online.controller.article_and_community;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCommentUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleCommentService;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/article-comments")
public class ArticleCommentController {

    ArticleCommentService articleCommentService;

    @PostMapping("/{articleId}")
    ApiResponse<ArticleCommentResponse> createComment(
            @PathVariable String articleId, @RequestBody @Valid ArticleCommentCreationRequest request) {
        return ApiResponse.<ArticleCommentResponse>builder()
                .result(articleCommentService.createComment(request, articleId))
                .build();
    }

    // Support legacy path /{articleId}/{userId} for backward compatibility
    @PostMapping("/{articleId}/{userId}")
    ApiResponse<ArticleCommentResponse> createComment(
            @RequestBody @Valid ArticleCommentCreationRequest request,
            @PathVariable String articleId,
            @PathVariable String userId) {
        return ApiResponse.<ArticleCommentResponse>builder()
                .result(articleCommentService.createComment(request, articleId, userId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ArticleCommentResponse> getComment(@PathVariable String id) {
        return ApiResponse.<ArticleCommentResponse>builder()
                .result(articleCommentService.getComment(id))
                .build();
    }

    @GetMapping("/article/{articleId}")
    ApiResponse<List<ArticleCommentResponse>> getCommentsByArticle(@PathVariable String articleId) {
        return ApiResponse.<List<ArticleCommentResponse>>builder()
                .result(articleCommentService.getCommentsByArticle(articleId))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ArticleCommentResponse> updateComment(
            @PathVariable String id, @RequestBody @Valid ArticleCommentUpdateRequest request) {
        return ApiResponse.<ArticleCommentResponse>builder()
                .result(articleCommentService.updateComment(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteComment(@PathVariable String id) {
        articleCommentService.deleteComment(id);
        return ApiResponse.<Void>builder().message("Comment has been deleted").build();
    }
}
