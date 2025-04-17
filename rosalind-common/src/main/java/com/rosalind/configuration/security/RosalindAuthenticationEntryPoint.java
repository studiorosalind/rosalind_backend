package com.rosalind.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosalind.common.response.error.RosalindErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RosalindAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private static final String UNAUTHORIZED_MESSAGE = "인증에 실패했습니다.";

  @Override
  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    res.setContentType("application/json;charset=UTF-8");
    res.setStatus(HttpStatus.UNAUTHORIZED.value());
    res.getWriter().write(mapper.writeValueAsString(
      RosalindErrorResponse.from(UNAUTHORIZED_MESSAGE, authException.getMessage()))
    );
  }
}
