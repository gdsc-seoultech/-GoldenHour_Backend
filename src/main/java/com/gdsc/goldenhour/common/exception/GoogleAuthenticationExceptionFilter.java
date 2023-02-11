package com.gdsc.goldenhour.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.goldenhour.common.dto.ResponseDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GoogleAuthenticationExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomCommonException e) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(ResponseDto.fail(e.getStatus(), e.getHttpStatus(), e.getMessage()));
            response.getWriter().write(body);

            response.setStatus(e.getHttpStatus().value());
        }
    }
}
