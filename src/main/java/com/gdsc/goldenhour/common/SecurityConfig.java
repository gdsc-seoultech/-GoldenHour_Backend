package com.gdsc.goldenhour.common;

import com.gdsc.goldenhour.common.exception.GoogleAuthenticationExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GoogleAuthenticationFilter googleFilter;
    private final GoogleAuthenticationExceptionFilter googleExceptionFilter;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors();

        http.csrf().disable();

        http.formLogin().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

        http.authorizeRequests()
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers(HttpMethod.OPTIONS).permitAll()
                        .antMatchers("/disaster/**", "/guide/**").permitAll()
                        .antMatchers("/user/**").authenticated().and()
                        .addFilterBefore(googleFilter, UsernamePasswordAuthenticationFilter.class)
                        .addFilterBefore(googleExceptionFilter, GoogleAuthenticationFilter.class);

        return http.build();
    }

}
