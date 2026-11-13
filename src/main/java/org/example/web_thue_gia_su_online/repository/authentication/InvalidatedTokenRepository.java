package org.example.web_thue_gia_su_online.repository.authentication;

import java.util.Date;

import org.example.web_thue_gia_su_online.entity.authentication.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    @Modifying
    @Query("""
		DELETE FROM InvalidatedToken t
		WHERE t.expiryTime < :now
	""")
    int deleteExpiredTokens(@Param("now") Date now);
}
