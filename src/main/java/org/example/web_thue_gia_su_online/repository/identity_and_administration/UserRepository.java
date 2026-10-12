package org.example.web_thue_gia_su_online.repository.identity_and_administration;

import java.util.Optional;

import org.example.web_thue_gia_su_online.entity.identity_and_administration.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByDisplayName(String displayName);

    Optional<User> findByDisplayName(String displayName);
}
