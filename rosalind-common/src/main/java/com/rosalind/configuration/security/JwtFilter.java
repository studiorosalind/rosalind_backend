package com.rosalind.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosalind.common.message.ErrorCode;
import com.rosalind.common.response.error.RosalindErrorResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.rosalind.common.constant.Constants.publicPathAntPatterns;

public class JwtFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  private final MessageService messageService;
  private final TokenProvider tokenProvider;
  private final RosalindAuthenticationProvider authenticationProvider;

  private final AntPathMatcher antPathMatcher = new AntPathMatcher();

  public JwtFilter(
    MessageService messageService,
    TokenProvider tokenProvider,
    RosalindAuthenticationProvider authenticationProvider
  ) {
    this.messageService = messageService;
    this.tokenProvider = tokenProvider;
    this.authenticationProvider = authenticationProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws IOException, ServletException {
    String jwt = this.resolveToken(request);
    if (isPublicPath(request.getRequestURI())) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String idStr = this.tokenProvider.validateToken(jwt);

      Long rosalindUserId = Long.parseLong(idStr);

      Authentication authentication = this.authenticationProvider.getAuthentication(rosalindUserId);
      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      ObjectMapper mapper = new ObjectMapper();
      response.setStatus(401);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(mapper.writeValueAsString(RosalindErrorResponse.from(
        messageService.getErrorMessage(ErrorCode.UNAUTHORIZED_ACCESS.getMessageKey()), e.getMessage())));
    }
  }

  private boolean isPublicPath(String requestURI) {
    for (String publicPathPattern : publicPathAntPatterns) {
      if (antPathMatcher.match(publicPathPattern, requestURI)) {
        return true;
      }
      if (publicPathPattern.equals(requestURI)) {
        return true;
      }
    }
    return false;
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    return StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX) ? bearerToken.substring(7) : null;
  }

}
