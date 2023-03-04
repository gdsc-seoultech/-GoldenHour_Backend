package com.gdsc.goldenhour.messageReport.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.messageReport.domain.Information;
import com.gdsc.goldenhour.messageReport.domain.ReportType;
import com.gdsc.goldenhour.messageReport.domain.Situation;
import com.gdsc.goldenhour.messageReport.dto.response.DetailSituationRes;
import com.gdsc.goldenhour.messageReport.dto.response.InformationRes;
import com.gdsc.goldenhour.messageReport.dto.response.SituationRes;
import com.gdsc.goldenhour.messageReport.dto.response.TypeSituationRes;
import com.gdsc.goldenhour.messageReport.repository.SituationAndInformationRepository;
import com.gdsc.goldenhour.messageReport.repository.SituationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageReportService {

    private final SituationRepository situationRepository;
    private final SituationAndInformationRepository situationAndInformationRepository;

    @Transactional(readOnly = true)
    public List<SituationRes> readSituationList(ReportType reportType) {
        List<SituationRes> response = new ArrayList<>();

        situationRepository.findByType(reportType).forEach(
                situation -> response.add(new SituationRes(situation))
        );

        return response;
    }

    @Transactional(readOnly = true)
    public List<TypeSituationRes> readTypeSituationList(Long situationId) {
        List<TypeSituationRes> response = new ArrayList<>();

        Situation situation = readSituation(situationId);

        situation.getTypeSituationList().forEach(
                typeSituation -> response.add(new TypeSituationRes(typeSituation))
        );

        return response;
    }

    @Transactional(readOnly = true)
    public List<DetailSituationRes> readDetailSituationList(Long situationId) {
        List<DetailSituationRes> response = new ArrayList<>();

        Situation situation = readSituation(situationId);

        situation.getDetailSituationList().forEach(
                detailSituation -> response.add(new DetailSituationRes(detailSituation))
        );

        return response;
    }

    @Transactional(readOnly = true)
    public List<InformationRes> readInformationList(Long situationId) {
        List<InformationRes> response = new ArrayList<>();

        Situation situation = readSituation(situationId);
        situationAndInformationRepository.findSituationAndInformationBySituation(situation).forEach(
                situationAndInformation -> {
                    Information information = situationAndInformation.getInformation();
                    InformationRes informationRes = new InformationRes(information);
                    information.getAnswerInformationList().forEach(
                            answerInformation -> {
                                informationRes.getAnswerList().add(answerInformation.getAnswer());
                            }
                    );
                    response.add(informationRes);
                }
        );

        return response;
    }

    public Situation readSituation(Long situationId) {
        return situationRepository.findSituationById(situationId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.ITEM_NOT_FOUND)
        );
    }

}
