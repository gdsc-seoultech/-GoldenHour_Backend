package com.gdsc.goldenhour.disaster.dto.response;

import com.gdsc.goldenhour.disaster.domain.COCImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class COCImageRes {

    private Long id;

    private String imgUrl;

    public COCImageRes(COCImage cocImage) {
        this.id = cocImage.getId();
        this.imgUrl = cocImage.getImgUrl();
    }
}
