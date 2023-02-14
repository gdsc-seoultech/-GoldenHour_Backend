package com.gdsc.goldenhour.user.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.user.domain.ReliefGoods;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.dto.request.ReliefGoodsReq;
import com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes;
import com.gdsc.goldenhour.user.repository.ReliefGoodsRepository;
import com.gdsc.goldenhour.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReliefGoodsService {

    private final UserRepository userRepository;
    private final ReliefGoodsRepository reliefGoodsRepository;

    @Transactional(readOnly = true)
    public List<ReliefGoodsRes> readReliefGoodsList(String userId) {
        User user = readUser(userId);

        List<ReliefGoodsRes> response = new ArrayList<>();
        user.getReliefGoodsList().forEach(
                reliefGoods -> response.add(reliefGoods.toReliefGoodsRes())
        );
        return response;
    }

    @Transactional
    public ReliefGoods createReliefGoods(ReliefGoodsReq request, String userId) {
        User user = readUser(userId);

        ReliefGoods reliefGoods = request.toReliefGoods();
        reliefGoods.setUser(user);

        return reliefGoods;
    }

    @Transactional
    public ReliefGoods updateReliefGoods(ReliefGoodsReq request, Long reliefGoodsId, String userId) {
        ReliefGoods reliefGoods = readReliefGoods(reliefGoodsId);
        validateUser(userId, reliefGoods);

        reliefGoods.update(request);

        return reliefGoods;
    }

    @Transactional
    public void deleteReliefGoods(Long reliefGoodsId, String userId) {
        ReliefGoods reliefGoods = readReliefGoods(reliefGoodsId);
        validateUser(userId, reliefGoods);

        reliefGoodsRepository.deleteById(reliefGoodsId);
    }

    private ReliefGoods readReliefGoods(Long reliefGoodsId) {
        return reliefGoodsRepository.findById(reliefGoodsId)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.ITEM_NOT_FOUND));
    }

    private User readUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomCommonException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateUser(String userId, ReliefGoods reliefGoods) {
        User user = readUser(userId);
        if (!reliefGoods.getUser().equals(user)) {
            throw new CustomCommonException(ErrorCode.INVALID_USER);
        }
    }

}
