package com.gdsc.goldenhour.user.domain;

import com.gdsc.goldenhour.user.dto.request.ReliefGoodsReq;
import com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "relief_goods")
public class ReliefGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    @Builder
    public ReliefGoods(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        user.getReliefGoodsList().add(this);
        this.user = user;
    }

    public void update(ReliefGoodsReq request) {
        name = request.getName();
    }

    public ReliefGoodsRes toReliefGoodsRes() {
        return ReliefGoodsRes.builder()
                .id(id)
                .name(name)
                .build();
    }

}
