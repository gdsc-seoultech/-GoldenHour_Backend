package com.gdsc.goldenhour.user.dto.request;

import com.gdsc.goldenhour.user.domain.ReliefGoods;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReliefGoodsReq {

    private String name;

    @Builder
    public ReliefGoodsReq(String name) {
        this.name = name;
    }

    public ReliefGoods toReliefGoods() {
        return ReliefGoods.builder()
                .name(name)
                .build();
    }
}
