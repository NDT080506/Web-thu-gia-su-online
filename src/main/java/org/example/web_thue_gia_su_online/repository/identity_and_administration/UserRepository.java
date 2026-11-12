package org.example.web_thue_gia_su_online.repository.identity_and_administration;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByDisplayName(String displayName);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByDisplayName(String displayName);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.identity_and_administration.UserResponse(
			u.id,
			u.email,
			u.displayName,
			u.avatarUrl,
			u.role,
			u.status,
			u.phone,
			u.zaloLink,
			u.createdAt,
			u.updatedAt
		)
		FROM User u
		""")
    Page<UserResponse> findResponses(Pageable pageable);
}
