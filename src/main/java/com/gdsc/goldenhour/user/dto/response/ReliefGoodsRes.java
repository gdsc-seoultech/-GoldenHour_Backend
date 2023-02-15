package com.gdsc.goldenhour.user.dto.response;

import com.gdsc.goldenhour.user.domain.ReliefGoods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ReliefGoodsRes {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReliefGoodsReadRes {
        private Long id;

        private String name;

        public ReliefGoodsReadRes(ReliefGoods reliefGoods) {
            this.id = reliefGoods.getId();
            this.name = reliefGoods.getName();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReliefGoodsCreateRes {
        private String name;

        public ReliefGoodsCreateRes(ReliefGoods reliefGoods) {
            this.name = reliefGoods.getName();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ReliefGoodsUpdateRes {
        private String name;

        public ReliefGoodsUpdateRes(ReliefGoods reliefGoods) {
            this.name = reliefGoods.getName();
        }
    }


}
