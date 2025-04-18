package com.rosalind.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosalind.common.message.ErrorCode;
import com.rosalind.common.response.error.RosalindErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RosalindAccessDeniedHandler implements AccessDeniedHandler {

  private final MessageService messageService;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getWriter().write(mapper.writeValueAsString(
      RosalindErrorResponse.from(messageService.getErrorMessage(ErrorCode.FORBIDDEN.getMessageKey()), ex.getMessage()))
    );
  }

}
