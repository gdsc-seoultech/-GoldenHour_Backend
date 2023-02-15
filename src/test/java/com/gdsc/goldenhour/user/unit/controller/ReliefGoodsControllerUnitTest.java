package com.gdsc.goldenhour.user.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.goldenhour.common.GoogleAuthenticationFilter;
import com.gdsc.goldenhour.common.GoogleIdTokenProvider;
import com.gdsc.goldenhour.common.exception.GoogleAuthenticationExceptionFilter;
import com.gdsc.goldenhour.user.controller.ReliefGoodsController;
import com.gdsc.goldenhour.user.dto.request.ReliefGoodsReq;
import com.gdsc.goldenhour.user.service.ReliefGoodsService;
import com.gdsc.goldenhour.user.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.gdsc.goldenhour.user.dto.response.ReliefGoodsRes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReliefGoodsController.class)
@Import({GoogleAuthenticationExceptionFilter.class, GoogleAuthenticationFilter.class})
public class ReliefGoodsControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReliefGoodsService reliefGoodsService;

    @MockBean
    private GoogleIdTokenProvider googleIdTokenProvider;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void 사용자_설정() {
        String userIdToken = "userIdToken";
        String userId = "userId";

        when(googleIdTokenProvider.provideGoogleId(userIdToken)).thenReturn(userId);
        when(userService.login(userId)).thenReturn(userId);
    }

    @Test
    @WithMockUser(username = "userId", authorities={"ROLE_USER"})
    public void 구호물품_조회() throws Exception {
        // given
        String userIdToken = "userIdToken";
        String userId = "userId";

        ReliefGoodsReadRes reliefGoodsReadRes_1 = ReliefGoodsReadRes.builder()
                .id(1L)
                .name("구호물품 1 이름")
                .build();
        ReliefGoodsReadRes reliefGoodsReadRes_2 = ReliefGoodsReadRes.builder()
                .name("구호물품 2 이름")
                .id(2L)
                .build();
        List<ReliefGoodsReadRes> reliefGoodsReadResList = new ArrayList<>(List.of(reliefGoodsReadRes_1, reliefGoodsReadRes_2));

        // stub
        when(reliefGoodsService.readReliefGoodsList(userId)).thenReturn(reliefGoodsReadResList);

        // when
        ResultActions resultActions = mockMvc.perform(get("/user/relief_goods")
                .header("Authorization", userIdToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data.[0].id").value(1))
                .andExpect(jsonPath("$.data.[0].name").value("구호물품 1 이름"))
                .andExpect(jsonPath("$.data.error").doesNotExist())
                .andDo(print());

    }

    @Test
    @WithMockUser(username = "userId", authorities={"ROLE_USER"})
    public void 구호물품_저장() throws Exception {
        // given
        String userIdToken = "userIdToken";
        String userId = "userId";

        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(reliefGoodsReq);

        ReliefGoodsCreateRes reliefGoodsCreateRes = ReliefGoodsCreateRes.builder()
                .name("구호물품 이름")
                .build();

        // stub
        when(reliefGoodsService.createReliefGoods(any(ReliefGoodsReq.class), eq(userId))).thenReturn(reliefGoodsCreateRes);

        // when
        ResultActions resultActions = mockMvc.perform(post("/user/relief_goods")
                .with(csrf())
                .header("Authorization", userIdToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("구호물품 이름"))
                .andExpect(jsonPath("$.data.error").doesNotExist())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "userId", authorities={"ROLE_USER"})
    public void 구호물품_수정() throws Exception {
        // given
        String userIdToken = "userIdToken";
        String userId = "userId";
        Long reliefGoodsId = 1L;

        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름 수정")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(reliefGoodsReq);

        ReliefGoodsUpdateRes reliefGoodsUpdateRes = ReliefGoodsUpdateRes.builder()
                .name("구호물품 이름 수정")
                .build();

        // stub
        when(reliefGoodsService.updateReliefGoods(any(ReliefGoodsReq.class), eq(reliefGoodsId), eq(userId))).thenReturn(reliefGoodsUpdateRes);

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/relief_goods/{id}", reliefGoodsId)
                .with(csrf())
                .header("Authorization", userIdToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("구호물품 이름 수정"))
                .andExpect(jsonPath("$.data.error").doesNotExist())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "userId", authorities={"ROLE_USER"})
    public void 구호물품_삭제() throws Exception {
        // given
        String userIdToken = "userIdToken";
        String userId = "userId";
        Long reliefGoodsId = 1L;

        // stub
        doNothing().when(reliefGoodsService).deleteReliefGoods(reliefGoodsId, userId);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/user/relief_goods/{id}", reliefGoodsId)
                .with(csrf())
                .header("Authorization", userIdToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("구호물품 삭제 완료"))
                .andDo(print());
    }
}
