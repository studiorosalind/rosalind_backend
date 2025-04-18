package com.rosalind.configuration.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Builder
public class RosalindAuthentication implements Authentication {

  private Object principal; // rosalind info
  private Object credentials; // password, or token for social login
  private Object details;
  private boolean authenticated;
  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public String getName() {
    Optional<Object> principal = Optional.ofNullable(this.principal);

    return ;
  }
}
