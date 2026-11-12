package org.example.web_thue_gia_su_online.repository.finance_and_wallet;

import org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse;
import org.example.web_thue_gia_su_online.entity.finance_and_wallet.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
    Page<PaymentMethod> findByUserId(String userId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.finance_and_wallet.PaymentMethodResponse(
			pm.id,
			pm.user.id,
			pm.bankName,
			pm.accountNumber,
			pm.accountHolderName,
			pm.isDefault
		)
		FROM PaymentMethod pm
		WHERE pm.user.id = :userId
		""")
    Page<PaymentMethodResponse> findResponsesByUserId(@Param("userId") String userId, Pageable pageable);
}
