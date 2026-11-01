package org.example.web_thue_gia_su_online.repository.learning_and_education;

import java.util.List;

import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokeragePackageRepository extends JpaRepository<BrokeragePackage, String> {
    List<BrokeragePackage> findByTutorId(String tutorId);

    List<BrokeragePackage> findByTutorIdAndActiveTrue(String tutorId);
}
