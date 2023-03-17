package com.gdsc.goldenhour.user.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.goldenhour.common.GoogleIdTokenProvider;
import com.gdsc.goldenhour.user.dto.request.EmergencyContactReq;
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
public class EmergencyContactIntegrationTest {

    @MockBean
    private GoogleIdTokenProvider googleIdTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 비상연락망_조회() throws Exception {
        // given
        String idToken = "validIdToken";

        // stub
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(get("/user/emergency_contact")
                .header("Authorization", idToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("비상연락망 1 이름"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("비상연락망 2 이름"))
                .andExpect(jsonPath("$.error").doesNotExist());

    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 비상연락망_저장() throws Exception {
        // given
        String idToken = "validIdToken";
        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 3 이름")
                .phoneNumber("010-3333-3333")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(emergencyContactReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(post("/user/emergency_contact")
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("비상연락망 3 이름"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-3333-3333"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 비상연락망_저장_INVALID_PARAM() throws Exception {
        // given
        String idToken = "validIdToken";
        EmergencyContactReq invalid_emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 3 이름")
                .phoneNumber("010-33-33")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(invalid_emergencyContactReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(post("/user/emergency_contact")
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value("400"))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.error.message").value("phoneNumber : 10 ~ 11 자리의 숫자만 입력 가능합니다."));
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 비상연락망_수정() throws Exception {
        // given
        String idToken = "validIdToken";
        Long emergencyContactId = 1L;
        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-3333-3333")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(emergencyContactReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/emergency_contact/{id}", emergencyContactId)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("비상연락망 이름 수정"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-3333-3333"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 비상연락망_수정_INVALID_PARAM() throws Exception {
        // given
        String idToken = "validIdToken";
        Long emergencyContactId = 1L;
        EmergencyContactReq invalid_emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-33-33")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(invalid_emergencyContactReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/emergency_contact/{id}", emergencyContactId)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value("400"))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.error.message").value("phoneNumber : 10 ~ 11 자리의 숫자만 입력 가능합니다."));
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 비상연락망_수정_ITEM_NOT_FOUND() throws Exception {
        // given
        String idToken = "validIdToken";
        Long not_found_emergencyContactId = 3L;
        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-3333-3333")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(emergencyContactReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/emergency_contact/{id}", not_found_emergencyContactId)
                .header("Authorization", idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value("404"))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.error.message").value("존재하지 않는 항목입니다."));
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 비상연락망_수정_INVALID_USER() throws Exception {
        // given
        String idToken = "validIdToken";
        Long emergencyContactId = 1L;
        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-3333-3333")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(emergencyContactReq);

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("invalidUserId");

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/emergency_contact/{id}", emergencyContactId)
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
    public void 비상연락망_삭제() throws Exception{
        // given
        String idToken = "validIdToken";
        Long emergencyContactId = 1L;

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(delete("/user/emergency_contact/{id}", emergencyContactId)
                .header("Authorization", idToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("비상 연락망 삭제 완료"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Sql({"classpath:testdb/data.sql"})
    @Test
    public void 비상연락망_삭제_ITEM_NOT_FOUND() throws Exception{
        // given
        String idToken = "validIdToken";
        Long not_found_emergencyContactId = 3L;

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(delete("/user/emergency_contact/{id}", not_found_emergencyContactId)
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
    public void 비상연락망_삭제_INVALID_USER() throws Exception{
        // given
        String idToken = "validIdToken";
        Long emergencyContactId = 1L;

        // stub - 동작 지정
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("invalidUserId");

        // when
        ResultActions resultActions = mockMvc.perform(delete("/user/emergency_contact/{id}", emergencyContactId)
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
