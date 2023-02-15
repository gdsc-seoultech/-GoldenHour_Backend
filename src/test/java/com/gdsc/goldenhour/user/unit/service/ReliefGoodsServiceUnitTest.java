package com.gdsc.goldenhour.user.unit.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.user.domain.ReliefGoods;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.dto.request.ReliefGoodsReq;
import com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.ReliefGoodsReadRes;
import com.gdsc.goldenhour.user.repository.ReliefGoodsRepository;
import com.gdsc.goldenhour.user.repository.UserRepository;
import com.gdsc.goldenhour.user.service.ReliefGoodsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.ReliefGoodsCreateRes;
import static com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.ReliefGoodsUpdateRes;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReliefGoodsServiceUnitTest {

    @InjectMocks
    private ReliefGoodsService reliefGoodsService;

    @Mock
    ReliefGoodsRepository reliefGoodsRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void 구호물품_조회() {
        // given
        User user = User.builder()
                .googleId("userId")
                .build();
        ReliefGoods reliefGoods_1 = ReliefGoods.builder()
                .name("구호물품 1 이름")
                .build();
        user.addReliefGoods(reliefGoods_1);
        ReliefGoods reliefGoods_2 = ReliefGoods.builder()
                .name("구호물품 2 이름")
                .build();
        user.addReliefGoods(reliefGoods_2);


        // stub - 동작 지정
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));

        // when
        List<ReliefGoodsReadRes> reliefGoodsReadResList = reliefGoodsService.readReliefGoodsList("userId");

        // then
        Assertions.assertThat(reliefGoodsReadResList).size().isEqualTo(2);

        Assertions.assertThat(reliefGoodsReadResList.get(0).getName()).isEqualTo("구호물품 1 이름");
        Assertions.assertThat(reliefGoodsReadResList.get(1).getName()).isEqualTo("구호물품 2 이름");
    }

    @Test
    public void 구호물품_조회_USER_NOT_FOUND() {
        // given

        // stub - 동작 지정
        when(userRepository.findById("notFoundUserId")).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> reliefGoodsService.readReliefGoodsList("notFoundUserId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

    @Test
    public void 구호물품_저장() {
        // given
        User user = User.builder()
                .googleId("userId")
                .build();

        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름").build();

        // stub - 동작 지정
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));

        // when
        ReliefGoodsCreateRes reliefGoodsCreateRes = reliefGoodsService.createReliefGoods(reliefGoodsReq, "userId");

        // then
        Assertions.assertThat(reliefGoodsCreateRes.getName()).isEqualTo("구호물품 이름");
    }

    @Test
    public void 구호물품_저장_USER_NOT_FOUND_ERROR() {
        // given
        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름").build();

        // stub - 동작 지정
        when(userRepository.findById("notFoundUserId")).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> reliefGoodsService.createReliefGoods(reliefGoodsReq, "notFoundUserId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("존재하지 않는 사용자입니다.");

    }

    @Test
    public void 구호물품_수정() {
        // given
        Long reliefGoodsId = 1L;
        ReliefGoods reliefGoods = ReliefGoods.builder()
                .name("구호물품 이름")
                .build();
        User user = User.builder()
                .googleId("userId")
                .build();
        user.addReliefGoods(reliefGoods);

        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름 수정").build();

        // stub - 동작 지정
        when(reliefGoodsRepository.findById(reliefGoodsId)).thenReturn(Optional.of(reliefGoods));
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));

        // when
        ReliefGoodsUpdateRes reliefGoodsUpdateRes = reliefGoodsService.updateReliefGoods(reliefGoodsReq, reliefGoodsId, "userId");

        // then
        Assertions.assertThat(reliefGoodsUpdateRes.getName()).isEqualTo("구호물품 이름 수정");
        Assertions.assertThat(user.getReliefGoodsList().get(0).getName()).isEqualTo("구호물품 이름 수정");
    }

    @Test
    public void 구호물품_수정_ITEM_NOT_FOUND() {
        // given
        Long not_found_reliefGoodsId = 1L;
        User user = User.builder()
                .googleId("userId")
                .build();

        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름 수정").build();

        // stub - 동작 지정
        when(reliefGoodsRepository.findById(not_found_reliefGoodsId)).thenReturn(Optional.empty());
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));

        // when & then
        Assertions.assertThatThrownBy(() -> reliefGoodsService.updateReliefGoods(reliefGoodsReq, not_found_reliefGoodsId, "userId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("존재하지 않는 항목입니다.");
    }

    @Test
    public void 구호물품_수정_INVALID_USER() {
        // given
        Long reliefGoodsId = 1L;
        ReliefGoods reliefGoods = ReliefGoods.builder()
                .name("구호물품 이름")
                .build();
        User user = User.builder()
                .googleId("userId")
                .build();
        user.addReliefGoods(reliefGoods);
        User invalidUser = User.builder()
                .googleId("invalidUserId")
                .build();

        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름 수정").build();

        // stub - 동작 지정
        when(reliefGoodsRepository.findById(reliefGoodsId)).thenReturn(Optional.of(reliefGoods));
        when(userRepository.findById("invalidUserId")).thenReturn(Optional.of(invalidUser));

        // when & then
        Assertions.assertThatThrownBy(() -> reliefGoodsService.updateReliefGoods(reliefGoodsReq, reliefGoodsId, "invalidUserId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("올바른 사용자가 아닙니다.");
    }

    @Test
    public void 구호물품_삭제() {
        // given
        User user = User.builder()
                .googleId("userId")
                .build();
        Long reliefGoods_1_id = 1L;
        ReliefGoods reliefGoods_1 = ReliefGoods.builder()
                .name("구호물품 1 이름")
                .build();
        user.addReliefGoods(reliefGoods_1);
        ReliefGoods reliefGoods_2 = ReliefGoods.builder()
                .name("구호물품 2 이름")
                .build();
        user.addReliefGoods(reliefGoods_2);


        // stub - 동작 지정
        when(reliefGoodsRepository.findById(reliefGoods_1_id)).thenReturn(Optional.of(reliefGoods_1));
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));

        // when
        reliefGoodsService.deleteReliefGoods(reliefGoods_1_id, "userId");
        List<ReliefGoodsReadRes> reliefGoodsReadResList = reliefGoodsService.readReliefGoodsList("userId");

        // then
        Assertions.assertThat(reliefGoodsReadResList).size().isEqualTo(1);

        Assertions.assertThat(reliefGoodsReadResList).doesNotContain(new ReliefGoodsReadRes(reliefGoods_1));
        Assertions.assertThat(reliefGoodsReadResList.get(0).getName()).isEqualTo("구호물품 2 이름");
    }

    @Test
    public void 구호물품_삭제_ITEM_NOT_FOUND() {
        // given
        Long not_found_reliefGoodsId = 1L;
        User user = User.builder()
                .googleId("userId")
                .build();

        // stub - 동작 지정
        when(reliefGoodsRepository.findById(not_found_reliefGoodsId)).thenReturn(Optional.empty());
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));

        // when & then
        Assertions.assertThatThrownBy(() -> reliefGoodsService.deleteReliefGoods(not_found_reliefGoodsId, "userId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("존재하지 않는 항목입니다.");
    }

    @Test
    public void 구호물품_삭제_INVALID_USER() {
        // given
        Long reliefGoodsId = 1L;
        ReliefGoods reliefGoods = ReliefGoods.builder()
                .name("구호물품 이름")
                .build();
        User user = User.builder()
                .googleId("userId")
                .build();
        user.addReliefGoods(reliefGoods);
        User invalidUser = User.builder()
                .googleId("invalidUserId")
                .build();

        // stub - 동작 지정
        when(reliefGoodsRepository.findById(reliefGoodsId)).thenReturn(Optional.of(reliefGoods));
        when(userRepository.findById("invalidUserId")).thenReturn(Optional.of(invalidUser));

        // when & then
        Assertions.assertThatThrownBy(() -> reliefGoodsService.deleteReliefGoods(reliefGoodsId, "invalidUserId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("올바른 사용자가 아닙니다.");
    }
}
