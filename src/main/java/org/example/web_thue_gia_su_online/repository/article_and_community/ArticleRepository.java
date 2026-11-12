package org.example.web_thue_gia_su_online.repository.article_and_community;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse;
import org.example.web_thue_gia_su_online.entity.article_and_community.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    Page<Article> findByIsPublishedTrueAndMinRoleLevelLessThanEqualOrderByCreatedAtDesc(
            int roleLevel, Pageable pageable);

    Page<Article> findByAuthorIdOrderByCreatedAtDesc(String authorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse(
			a.id,
			a.author.id,
			a.authorNickname,
			a.authorAvatarUrl,
			a.title,
			a.content,
			a.minRoleLevel,
			a.isPublished,
			a.createdAt,
			a.updatedAt
		)
		FROM Article a
		WHERE a.id = :id
		""")
    Optional<ArticleResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.article_and_community.ArticleResponse(
			a.id,
			a.author.id,
			a.authorNickname,
			a.authorAvatarUrl,
			a.title,
			a.content,
			a.minRoleLevel,
			a.isPublished,
			a.createdAt,
			a.updatedAt
		)
		FROM Article a
		WHERE a.isPublished = true AND a.minRoleLevel <= :roleLevel
		ORDER BY a.createdAt DESC
		""")
    Page<ArticleResponse> findPublishedResponsesByRoleLevel(@Param("roleLevel") int roleLevel, Pageable pageable);
}
