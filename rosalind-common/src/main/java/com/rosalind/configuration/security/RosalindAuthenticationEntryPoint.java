package com.rosalind.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosalind.common.message.ErrorCode;
import com.rosalind.common.response.error.RosalindErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RosalindAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final MessageService messageService;

  @Override
  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    res.setContentType("application/json;charset=UTF-8");
    res.setStatus(HttpStatus.UNAUTHORIZED.value());
    res.getWriter().write(mapper.writeValueAsString(
      RosalindErrorResponse.from(messageService.getErrorMessage(ErrorCode.UNAUTHORIZED_ACCESS.getMessageKey()),
        authException.getMessage()))
    );
  }
}
