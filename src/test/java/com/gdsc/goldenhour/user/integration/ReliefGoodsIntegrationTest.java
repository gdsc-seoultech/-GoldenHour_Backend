package com.gdsc.goldenhour.user.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.goldenhour.common.GoogleIdTokenProvider;
import com.gdsc.goldenhour.user.dto.request.ReliefGoodsReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReliefGoodsIntegrationTest {

    @MockBean
    private GoogleIdTokenProvider googleIdTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 구호물품_조회() throws Exception {
        // given
        String idToken = "validIdToken";

        // stub
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(get("/user/relief_goods")
                .header("Authorization", idToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("구호물품 1 이름"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("구호물품 2 이름"))
                .andExpect(jsonPath("$.error").doesNotExist());

    }

    @Sql({"classpath:testdb/data-only-user.sql"})
    @Test
    public void 구호물품_저장() throws Exception {
        // given
        String idToken = "validIdToken";
        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 3 이름")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(reliefGoodsReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(post("/user/relief_goods")
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("구호물품 3 이름"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 구호물품_수정() throws Exception {
        // given
        String idToken = "validIdToken";
        Long reliefGoodsId = 1L;
        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름 수정")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(reliefGoodsReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/relief_goods/{id}", reliefGoodsId)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("구호물품 이름 수정"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 구호물품_수정_ITEM_NOT_FOUND() throws Exception {
        // given
        String idToken = "validIdToken";
        Long not_found_reliefGoodsId = 3L;
        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름 수정")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(reliefGoodsReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/relief_goods/{id}", not_found_reliefGoodsId)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value(404))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.error.message").value("존재하지 않는 항목입니다."));
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 구호물품_수정_INVALID_USER() throws Exception {
        // given
        String idToken = "validIdToken";
        Long reliefGoodsId = 1L;
        ReliefGoodsReq reliefGoodsReq = ReliefGoodsReq.builder()
                .name("구호물품 이름 수정")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(reliefGoodsReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("invalidUserId");

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/relief_goods/{id}", reliefGoodsId)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value(403))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.FORBIDDEN.name()))
                .andExpect(jsonPath("$.error.message").value("올바른 사용자가 아닙니다."));
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 구호물품_삭제() throws Exception{
        // given
        String idToken = "validIdToken";
        Long reliefGoodsId = 1L;

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(delete("/user/relief_goods/{id}", reliefGoodsId)
                .header("Authorization", idToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("구호물품 삭제 완료"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 구호물풀_삭제_ITEM_NOT_FOUND() throws Exception {
        // given
        String idToken = "validIdToken";
        Long not_found_reliefGoodsId = 3L;

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(delete("/user/relief_goods/{id}", not_found_reliefGoodsId)
                .header("Authorization", idToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value(404))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.error.message").value("존재하지 않는 항목입니다."));
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 구호물풀_삭제_INVALID_USER() throws Exception {
        // given
        String idToken = "validIdToken";
        Long reliefGoodsId = 1L;

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("invalidUserId");

        // when
        ResultActions resultActions = mockMvc.perform(delete("/user/relief_goods/{id}", reliefGoodsId)
                .header("Authorization", idToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value(403))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.FORBIDDEN.name()))
                .andExpect(jsonPath("$.error.message").value("올바른 사용자가 아닙니다."));
    }

}
