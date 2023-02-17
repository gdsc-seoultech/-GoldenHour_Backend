package com.gdsc.goldenhour.disaster.dto.response;

import com.gdsc.goldenhour.disaster.domain.Disaster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DisasterRes {

    private Long id;

    private String name;

    public DisasterRes(Disaster disaster) {
        this.id = disaster.getId();
        this.name = disaster.getName();
    }
}
