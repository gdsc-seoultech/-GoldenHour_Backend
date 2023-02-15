package com.gdsc.goldenhour.user.repository;

import com.gdsc.goldenhour.user.domain.ReliefGoods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReliefGoodsRepository extends JpaRepository<ReliefGoods, Long> {

    List<ReliefGoods> findAllByUser_GoogleId(String googleId);
}
