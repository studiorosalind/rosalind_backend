package com.rosalind.configuration.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final TokenProvider tokenProvider;
  private final RosalindAuthenticationProvider authenticationProvider;

  public void configure(HttpSecurity http) {
    JwtFilter customFilter = new JwtFilter(tokenProvider, authenticationProvider);
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }

  public JwtSecurityConfig(final TokenProvider tokenProvider, final RosalindAuthenticationProvider authenticationProvider) {
    this.tokenProvider = tokenProvider;
    this.authenticationProvider = authenticationProvider;
  }

}
