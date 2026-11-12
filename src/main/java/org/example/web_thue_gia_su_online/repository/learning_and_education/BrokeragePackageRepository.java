package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokeragePackageRepository extends JpaRepository<BrokeragePackage, String> {
    Page<BrokeragePackage> findByTutorId(String tutorId, Pageable pageable);

    Page<BrokeragePackage> findByTutorIdAndActiveTrue(String tutorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse(
			bp.id,
			bp.tutor.id,
			bp.name,
			bp.price,
			bp.platformFeePercent,
			bp.active
		)
		FROM BrokeragePackage bp
		WHERE bp.id = :id
		""")
    Optional<BrokeragePackageResponse> findResponseById(@Param("id") String id);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse(
			bp.id,
			bp.tutor.id,
			bp.name,
			bp.price,
			bp.platformFeePercent,
			bp.active
		)
		FROM BrokeragePackage bp
		WHERE bp.tutor.id = :tutorId
		""")
    Page<BrokeragePackageResponse> findResponsesByTutorId(@Param("tutorId") String tutorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse(
			bp.id,
			bp.tutor.id,
			bp.name,
			bp.price,
			bp.platformFeePercent,
			bp.active
		)
		FROM BrokeragePackage bp
		WHERE bp.tutor.id = :tutorId AND bp.active = true
		""")
    Page<BrokeragePackageResponse> findResponsesByTutorIdAndActiveTrue(
            @Param("tutorId") String tutorId, Pageable pageable);

    @Query("""
		SELECT new org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse(
			bp.id,
			bp.tutor.id,
			bp.name,
			bp.price,
			bp.platformFeePercent,
			bp.active
		)
		FROM BrokeragePackage bp
		""")
    Page<BrokeragePackageResponse> findResponsesAll(Pageable pageable);
}
