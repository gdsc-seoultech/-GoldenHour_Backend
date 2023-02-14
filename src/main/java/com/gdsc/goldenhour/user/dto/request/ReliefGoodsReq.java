package com.gdsc.goldenhour.user.dto.request;

import com.gdsc.goldenhour.user.domain.ReliefGoods;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ReliefGoodsReq {

    @NotBlank(message = "name : 이름을 입력해주세요.")
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
