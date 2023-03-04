package com.gdsc.goldenhour.messageReport.dto.response;

import com.gdsc.goldenhour.messageReport.domain.TypeSituation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TypeSituationRes {

    private Long id;

    private String name;

    public TypeSituationRes(TypeSituation typeSituation) {
        this.id = typeSituation.getId();
        this.name = typeSituation.getName();
    }
}
