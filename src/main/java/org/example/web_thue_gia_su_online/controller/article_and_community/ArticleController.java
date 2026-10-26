package org.example.web_thue_gia_su_online.controller.article_and_community;

import java.util.List;

import jakarta.validation.Valid;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/articles")
public class ArticleController {

    ArticleService articleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'TUTOR', 'ADMIN')")
    ApiResponse<ArticleResponse> createArticle(@RequestBody @Valid ArticleCreationRequest request) {
        return ApiResponse.<ArticleResponse>builder()
                .result(articleService.createArticle(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ArticleResponse> getArticle(@PathVariable String id) {
        return ApiResponse.<ArticleResponse>builder()
                .result(articleService.getArticle(id))
                .build();
    }

    @GetMapping
    ApiResponse<List<ArticleResponse>> getArticles() {
        return ApiResponse.<List<ArticleResponse>>builder()
                .result(articleService.getArticles())
                .build();
    }

    @GetMapping("/my-articles")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<List<ArticleResponse>> getMyArticles() {
        return ApiResponse.<List<ArticleResponse>>builder()
                .result(articleService.getMyArticles())
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<ArticleResponse> updateArticle(
            @PathVariable String id, @RequestBody @Valid ArticleUpdateRequest request) {
        return ApiResponse.<ArticleResponse>builder()
                .result(articleService.updateArticle(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    ApiResponse<Void> deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
        return ApiResponse.<Void>builder().message("Article has been deleted").build();
    }
}
