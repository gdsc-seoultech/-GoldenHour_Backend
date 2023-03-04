package com.gdsc.goldenhour.messageReport.dto.response;

import com.gdsc.goldenhour.messageReport.domain.DetailSituation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DetailSituationRes {

    private Long id;

    private String name;

    public DetailSituationRes(DetailSituation detailSituation) {
        this.id = detailSituation.getId();
        this.name = detailSituation.getName();
    }
}
