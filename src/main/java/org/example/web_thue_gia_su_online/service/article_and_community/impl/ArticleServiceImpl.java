package org.example.web_thue_gia_su_online.service.article_and_community.impl;

import java.time.Instant;
import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.article_and_community.ArticleUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.example.web_thue_gia_su_online.mapper.article_and_community.ArticleMapper;
import org.example.web_thue_gia_su_online.repository.article_and_community.ArticleRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.service.article_and_community.ArticleService;
import org.springframework.stereotype.Service;

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
    UserRepository userRepository;
    ArticleMapper articleMapper;

    @Override
    public ArticleResponse createArticle(ArticleCreationRequest request, String authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));

        Article article = articleMapper.toArticle(request);
        article.setAuthor(author);
        article.setCreatedAt(Instant.now());
        article.setUpdateAt(Instant.now());

        return articleMapper.toArticleResponse(articleRepository.save(article));
    }

    @Override
    public ArticleResponse getArticle(String id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));

        return articleMapper.toArticleResponse(article);
    }

    @Override
    public List<ArticleResponse> getArticles() {
        return articleRepository.findAll().stream()
                .map(articleMapper::toArticleResponse)
                .toList();
    }

    @Override
    public ArticleResponse updateArticle(ArticleUpdateRequest request, String id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));

        articleMapper.updateArticle(article, request);
        article.setUpdateAt(Instant.now());

        return articleMapper.toArticleResponse(articleRepository.save(article));
    }

    @Override
    public void deleteArticle(String id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));

        articleRepository.delete(article);
    }
}