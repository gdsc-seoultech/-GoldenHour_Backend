package com.gdsc.goldenhour.common;

import com.gdsc.goldenhour.common.exception.CustomCommonException;
import com.gdsc.goldenhour.common.exception.ErrorCode;
import com.gdsc.goldenhour.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GoogleAuthenticationFilter extends OncePerRequestFilter {


    private final GoogleIdTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/user")) {
            // google idToken 유효성 검증
            final String idToken = request.getHeader("Authorization"); // Authorization 헤더 꺼냄

            if (idToken != null) { // idToken 이 존재하는지 확인
                Optional<User> user = tokenProvider.convertUser(idToken);
                if (user.isPresent()) {
                    Authentication authentication = getAuthentication(user.get());
                    SecurityContextHolder.getContext().setAuthentication(authentication); // token에 존재하는 authentication 정보 삽입
                }
            } else {
                throw new CustomCommonException(ErrorCode.MISSING_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

}
