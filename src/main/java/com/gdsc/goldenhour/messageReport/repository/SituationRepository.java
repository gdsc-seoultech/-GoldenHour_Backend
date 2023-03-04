package com.gdsc.goldenhour.messageReport.repository;

import com.gdsc.goldenhour.messageReport.domain.ReportType;
import com.gdsc.goldenhour.messageReport.domain.Situation;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface SituationRepository extends Repository<Situation, Long> {
    Optional<Situation> findSituationById(Long id);
    List<Situation> findByType(ReportType type);
}
