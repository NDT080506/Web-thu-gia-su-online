package org.example.web_thue_gia_su_online.repository.article_and_community;

import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.ArticleComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, String> {
    Page<ArticleComment> findByArticleId(String articleId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "parent"})
    Page<ArticleComment> findByArticleIdOrderByCreatedAtAsc(String articleId, Pageable pageable);

    Page<ArticleComment> findByArticleIdAndParentIsNull(String articleId, Pageable pageable);

    Page<ArticleComment> findByParentId(String parentId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "parent"})
    Page<ArticleComment> findByParentIdOrderByCreatedAtAsc(String parentId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleCommentResponse(
			ac.id,
			ac.article.id,
			ac.user.id,
			ac.user.displayName,
			ac.user.avatarUrl,
			ac.parent.id,
			ac.content,
			ac.isHidden,
			ac.createdAt
		)
		FROM ArticleComment ac
		WHERE ac.article.id = :articleId
		ORDER BY ac.createdAt ASC
		""")
    Page<ArticleCommentResponse> findResponsesByArticleId(@Param("articleId") String articleId, Pageable pageable);
}
