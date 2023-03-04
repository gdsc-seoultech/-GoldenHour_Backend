package com.gdsc.goldenhour.messageReport.repository;

import com.gdsc.goldenhour.messageReport.domain.Situation;
import com.gdsc.goldenhour.messageReport.domain.SituationAndInformation;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface SituationAndInformationRepository extends Repository<SituationAndInformation, Long> {
    List<SituationAndInformation> findSituationAndInformationBySituation(Situation situation);
}
