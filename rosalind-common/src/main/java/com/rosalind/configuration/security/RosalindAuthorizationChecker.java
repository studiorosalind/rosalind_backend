package com.rosalind.configuration.security;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;

@Component
public class RosalindAuthorizationChecker {

  public boolean check(HttpServletRequest request, Authentication authentication) {
    return true;
  }


}
