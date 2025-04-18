package com.rosalind.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws IOException, ServletException {
    String jwt = this.resolveToken(request);
    if (isPublicPath(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

//    if (request.getRequestURI().contains(FRUITMAN_PREFIX)) {
//      this.handleFruitManRequest(jwt);
//      filterChain.doFilter(request, response);
//      return;
//    }

    try {
      String idStr = this.FleekTokenProvider.validateToken(jwt);

      Long FleekUserId = Long.parseLong(idStr);

      Authentication authentication = this.FleekAuthenticationProvider.getAuthentication(FleekUserId);
      SecurityContextHolder.getContext().setAuthentication(authentication);


      filterChain.doFilter(request, response);
    } catch (Exception e) {
      ObjectMapper mapper = new ObjectMapper();
      response.setStatus(401);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(mapper.writeValueAsString(FleekErrorResponse.from("Failed to authenticate", e.getMessage())));
    }
  }

}
