package com.gdsc.goldenhour.messageReport.dto.response;

import com.gdsc.goldenhour.messageReport.domain.Situation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SituationRes {

    private Long id;

    private String name;

    public SituationRes(Situation situation) {
        this.id = situation.getId();
        this.name = situation.getName();
    }
}
