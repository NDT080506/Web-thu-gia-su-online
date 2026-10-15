package org.example.web_thue_gia_su_online.repository.authentication;

import org.example.web_thue_gia_su_online.entity.authentication.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
