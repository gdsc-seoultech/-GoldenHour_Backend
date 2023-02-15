package com.gdsc.goldenhour.guide.dto.response;

import com.gdsc.goldenhour.guide.domain.Guide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GuideRes {

    private Long id;

    private String name;

    public GuideRes(Guide guide) {
        this.id = guide.getId();
        this.name = guide.getName();
    }
}
