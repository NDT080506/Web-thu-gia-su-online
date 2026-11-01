package org.example.web_thue_gia_su_online.repository.finance_and_wallet;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.example.web_thue_gia_su_online.entity.finance_and_wallet.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, String> {

    List<Withdrawal> findByUser_Id(String userId);

    Optional<Withdrawal> findByIdAndUser_Id(String id, String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
			SELECT w
			FROM Withdrawal w
			WHERE w.id = :id
			""")
    Optional<Withdrawal> findByIdForUpdate(@Param("id") String id);
}
