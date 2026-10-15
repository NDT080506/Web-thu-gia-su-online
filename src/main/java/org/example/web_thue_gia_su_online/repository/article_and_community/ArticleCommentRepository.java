package org.example.web_thue_gia_su_online.repository.article_and_community;

import java.util.List;

import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, String> {
    List<ArticleComment> findByArticleId(String articleId);

    List<ArticleComment> findByArticleIdAndParentIsNull(String articleId);

    List<ArticleComment> findByParentId(String parentId);
}
