package com.gdsc.goldenhour.user.unit.service;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.repository.UserRepository;
import com.gdsc.goldenhour.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void 사용자_조회() {
        // given
        User user = User.builder()
                .googleId("userId")
                .build();

        // stub - 동작 지정
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));

        // when
        User readUser = userService.readUser("userId");

        // then
        Assertions.assertThat(readUser.getGoogleId()).isEqualTo("userId");
    }

    @Test
    public void 사용자_조회_USER_NOT_FOUND() {
        // given

        // stub - 동작 지정
        when(userRepository.findById("notFoundUserId")).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> userService.readUser("notFoundUserId"))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }
}
