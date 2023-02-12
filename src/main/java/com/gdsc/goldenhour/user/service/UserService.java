package com.gdsc.goldenhour.user.service;

import com.gdsc.goldenhour.user.domain.User;
import com.gdsc.goldenhour.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public String login(String googleId) {
        // 회원 존재 여부 확인 및 가입
        User user =  userRepository.findById(googleId).orElse(
                userRepository.save(
                        User.builder()
                                .googleId(googleId)
                                .build()
                )
        );

        return user.getGoogleId();
    }
}
