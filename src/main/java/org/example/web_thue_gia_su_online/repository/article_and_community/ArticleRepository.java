package org.example.web_thue_gia_su_online.repository.article_and_community;

import java.util.List;

import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    List<Article> findByIsPublishedTrueAndMinRoleLevelLessThanEqualOrderByCreatedAtDesc(int roleLevel);

    List<Article> findByAuthorIdOrderByCreatedAtDesc(String authorId);
}
