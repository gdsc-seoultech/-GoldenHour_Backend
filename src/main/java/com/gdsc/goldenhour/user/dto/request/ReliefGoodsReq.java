package com.gdsc.goldenhour.user.dto.request;

import com.gdsc.goldenhour.user.domain.ReliefGoods;
import lombok.Getter;

@Getter
public class ReliefGoodsReq {

    private String name;

    public ReliefGoods toReliefGoods() {
        return ReliefGoods.builder()
                .name(name)
                .build();
    }
}
