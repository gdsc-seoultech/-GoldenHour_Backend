package com.gdsc.goldenhour.guide.dto.response;

import com.gdsc.goldenhour.guide.domain.GuideImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class GuideImageRes {

    private Long id;

    private String imgUrl;

    public GuideImageRes(GuideImage guideImage) {
        this.id = guideImage.getId();
        this.imgUrl = guideImage.getImgUrl();
    }
}
