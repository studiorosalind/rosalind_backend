package com.rosalind.configuration.security;

import com.rosalind.domain.user.RosalindUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RosalindAuthenticationProvider {

  private final RosalindUserService rosalindUserService;

  public Authentication getAuthentication(Long rosalindUserId) {

    return RosalindAuthentication.fromRosalindUser(rosalindUserService.getRosalindUser(rosalindUserId));
  }
}
