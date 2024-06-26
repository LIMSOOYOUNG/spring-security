package com.security.jwt.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity                                          // Spring Security 필터가 스프링 필터 체인에 등록이 된다.
@EnableGlobalMethodSecurity(prePostEnabled = true)          // 메소드 단위 권한 설정을 할 수 있다. @PreAuthorize, @PostAuthorize를 사용할 수 있음.
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()  // csrf  보안 x

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증 > 세션 필요없음

                .and()
                .authorizeRequests()    // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정
                .antMatchers("/auth/login").permitAll() // 모든 주소 허용
                .anyRequest().authenticated() // Authentication 필요한 주소

                .and()                  // exception handling for jwt
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        httpSecurity.apply(new JwtSecurityConfig(jwtTokenProvider));

        return httpSecurity.build();












    }

}
