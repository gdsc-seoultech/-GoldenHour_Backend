package com.gdsc.goldenhour.user.integration;

import com.gdsc.goldenhour.common.GoogleIdTokenProvider;
import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserIntegrationTest {

    @MockBean
    private GoogleIdTokenProvider googleIdTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 로그인_성공() throws Exception {
        // given
        String idToken = "validIdToken";

        // stub
        when(googleIdTokenProvider.provideGoogleId(idToken)).thenReturn("userId");

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .header("Authorization", idToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("로그인 완료"))
                .andExpect(jsonPath("$.error").doesNotExist());

        Optional<User> userOptional = userRepository.findById("userId");
        Assertions.assertThat(userOptional)
                .isPresent();
    }


    @Test
    public void idToken_존재_X() throws Exception {
        // given

        // stub
        when(googleIdTokenProvider.provideGoogleId(null)).thenThrow(new CustomCommonException(ErrorCode.MISSING_TOKEN));

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value(403))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.FORBIDDEN.name()))
                .andExpect(jsonPath("$.error.message").value("토큰이 존재하지 않습니다."));
    }

    @Test
    public void idToken_불일치() throws Exception {
        // given
        String idToken = "inValidIdToken";

        // stub
        when(googleIdTokenProvider.provideGoogleId("inValidIdToken")).thenThrow(new CustomCommonException(ErrorCode.INVALID_TOKEN));

        // when
        ResultActions resultActions = mockMvc.perform(post("/user")
                .header("Authorization", idToken)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error.status").value(403))
                .andExpect(jsonPath("$.error.httpStatus").value(HttpStatus.FORBIDDEN.name()))
                .andExpect(jsonPath("$.error.message").value("토큰이 유효하지 않습니다."));
    }
}
