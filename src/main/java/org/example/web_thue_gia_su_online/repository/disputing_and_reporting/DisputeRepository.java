package org.example.web_thue_gia_su_online.repository.disputing_and_reporting;

import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute, String> {}
