package com.gdsc.goldenhour.user.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.goldenhour.common.GoogleAuthenticationFilter;
import com.gdsc.goldenhour.common.GoogleIdTokenProvider;
import com.gdsc.goldenhour.common.exception.GoogleAuthenticationExceptionFilter;
import com.gdsc.goldenhour.user.controller.EmergencyContactController;
import com.gdsc.goldenhour.user.dto.request.EmergencyContactReq;
import com.gdsc.goldenhour.user.service.EmergencyContactService;
import com.gdsc.goldenhour.user.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.gdsc.goldenhour.user.dto.response.EmergencyContactRes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmergencyContactController.class)
@Import({GoogleAuthenticationExceptionFilter.class, GoogleAuthenticationFilter.class})
public class EmergencyContactControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmergencyContactService emergencyContactService;

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
    @WithMockUser(username = "userId", authorities = {"ROLE_USER"})
    public void 비상연락망_조회() throws Exception {
        // given
        String userIdToken = "userIdToken";
        String userId = "userId";

        EmergencyContactReadRes emergencyContactReadRes_1 = EmergencyContactReadRes.builder()
                .id(1L)
                .name("비상연락망 1 이름")
                .phoneNumber("010-1111-1111")
                .build();
        EmergencyContactReadRes emergencyContactReadRes_2 = EmergencyContactReadRes.builder()
                .id(2L)
                .name("비상연락망 2 이름")
                .phoneNumber("010-2222-2222")
                .build();
        List<EmergencyContactReadRes> emergencyContactReadResList = new ArrayList<>(List.of(emergencyContactReadRes_1, emergencyContactReadRes_2));

        // stub
        when(emergencyContactService.readEmergencyContactList(userId)).thenReturn(emergencyContactReadResList);

        // when
        ResultActions resultActions = mockMvc.perform(get("/user/emergency_contact")
                .header("Authorization", userIdToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data.[0].id").value(1))
                .andExpect(jsonPath("$.data.[0].name").value("비상연락망 1 이름"))
                .andExpect(jsonPath("$.data.[0].phoneNumber").value("010-1111-1111"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @WithMockUser(username = "userId", authorities = {"ROLE_USER"})
    public void 비상연락망_저장() throws Exception {
        // given
        String userIdToken = "userIdToken";
        String userId = "userId";

        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름")
                .phoneNumber("010-1111-1111")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(emergencyContactReq);

        EmergencyContactCreateRes emergencyContactCreateRes = EmergencyContactCreateRes.builder()
                .name("비상연락망 이름")
                .phoneNumber("010-1111-1111")
                .build();

        // stub
        when(emergencyContactService.createEmergencyContact(any(EmergencyContactReq.class), eq(userId))).thenReturn(emergencyContactCreateRes);

        // when
        ResultActions resultActions = mockMvc.perform(post("/user/emergency_contact")
                .with(csrf())
                .header("Authorization", userIdToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("비상연락망 이름"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-1111-1111"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @WithMockUser(username = "userId", authorities = {"ROLE_USER"})
    public void 비상연락망_저장_INVALID_PARAM() throws Exception {
        // given
        String userIdToken = "userIdToken";

        EmergencyContactReq invalid_emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름")
                .phoneNumber("010-11-11")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(invalid_emergencyContactReq);

        // when
        ResultActions resultActions = mockMvc.perform(post("/user/emergency_contact")
                .with(csrf())
                .header("Authorization", userIdToken)
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

    @Test
    @WithMockUser(username = "userId", authorities = {"ROLE_USER"})
    public void 비상연락망_수정() throws Exception {
        // given
        String userIdToken = "userIdToken";
        String userId = "userId";
        Long emergencyContactId = 1L;

        EmergencyContactReq emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-2222-2222")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(emergencyContactReq);

        EmergencyContactUpdateRes emergencyContactUpdateRes = EmergencyContactUpdateRes.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-2222-2222")
                .build();

        // stub
        when(emergencyContactService.updateEmergencyContact(any(EmergencyContactReq.class), eq(emergencyContactId), eq(userId))).thenReturn(emergencyContactUpdateRes);

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/emergency_contact/{id}", emergencyContactId)
                .with(csrf())
                .header("Authorization", userIdToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("비상연락망 이름 수정"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-2222-2222"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @WithMockUser(username = "userId", authorities = {"ROLE_USER"})
    public void 비상연락망_수정_INVALID_PARAM() throws Exception {
        // given
        String userIdToken = "userIdToken";
        Long emergencyContactId = 1L;

        EmergencyContactReq invalid_emergencyContactReq = EmergencyContactReq.builder()
                .name("비상연락망 이름 수정")
                .phoneNumber("010-22-22")
                .build();
        String requestBody = new ObjectMapper().writeValueAsString(invalid_emergencyContactReq);

        // when
        ResultActions resultActions = mockMvc.perform(put("/user/emergency_contact/{id}", emergencyContactId)
                .with(csrf())
                .header("Authorization", userIdToken)
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

    @Test
    @WithMockUser(username = "userId", authorities = {"ROLE_USER"})
    public void 비상연락망_삭제() throws Exception{
        // given
        String userIdToken = "userIdToken";
        String userId = "userId";
        Long emergencyContactId = 1L;

        // stub
        doNothing().when(emergencyContactService).deleteEmergencyContact(emergencyContactId, userId);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/user/emergency_contact/{id}", emergencyContactId)
                .with(csrf())
                .header("Authorization", userIdToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("비상 연락망 삭제 완료"));
    }
}
