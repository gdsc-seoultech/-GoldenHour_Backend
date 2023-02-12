package com.gdsc.goldenhour.common;

import com.gdsc.goldenhour.user.service.UserService;
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

@Component
@RequiredArgsConstructor
public class GoogleAuthenticationFilter extends OncePerRequestFilter {


    private final GoogleIdTokenProvider tokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/user")) {
            // google idToken 유효성 검증
            final String idToken = request.getHeader("Authorization"); // Authorization 헤더 꺼냄

            String googleId = tokenProvider.provideGoogleId(idToken);
            String userId = userService.login(googleId);

            Authentication authentication = getAuthentication(userId);
            SecurityContextHolder.getContext().setAuthentication(authentication); // token에 존재하는 authentication 정보 삽입
        }

        filterChain.doFilter(request, response);
    }

    // TODO: User 도메인으로 옮기기?
    private Authentication getAuthentication(String userId) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(userId, "", authorities);
    }

}
