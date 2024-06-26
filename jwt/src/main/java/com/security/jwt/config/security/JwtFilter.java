package com.security.jwt.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  /*GenericFilterBean 상속받아서 doFilter를 구현해도 되지만, OncePerRequestFilter를 상속받으면 doFilterInternal을 구현하면 된다.
  OncePerRequestFilter는 Filter를 상속받아서 doFilter를 구현할 때, 매번 Filter를 거치지 않도록 해준다. */

  private final JwtTokenProvider jwtTokenProvider;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    /* jwt 토큰을 꺼내옵니다. */
    String token = jwtTokenProvider.resolveToken(request);

    /* token을 갖고 있거나 토큰이 유효하거나 만료되었는지 체크한다.*/
    if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {


      Authentication authentication =  jwtTokenProvider.getAuthentication(token);
      // 토큰이 정상이면 토큰으로부터 유저 정보를 받아온다.
      // 유저 정보로 UsernamePasswordAuthenticationToken 객체를 생성한다.
      // UsernamePasswordAuthenticationToken 객체를 SecurityContext에 저장한다.
      SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    filterChain.doFilter(request, response);

  }


}
