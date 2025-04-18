package com.rosalind.api.configuration;

import com.rosalind.configuration.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;


@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@ComponentScan("com.rosalind.api")
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  private final MessageService messageService;
  private final TokenProvider tokenProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .cors()
      .and()
      .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
      .accessDeniedHandler(accessDeniedHandler())
      .and()
      .headers()
      .frameOptions()
      .sameOrigin()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .authorizeRequests()
      .antMatchers(HttpMethod.OPTIONS).permitAll()
      .antMatchers(
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/static/index.html",
        "/index.html"
      ).permitAll()
      .antMatchers(
        "/error**",
        "/hello/**",
        "/actuator/**"
      ).permitAll()
      .anyRequest().access("@rosalindAuthorizationChecker.check(request, authentication)")
      .and()
      .apply(new JwtSecurityConfig(tokenProvider))
      .and()
      .formLogin().disable()
    ;
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(){
    return new RosalindAuthenticationEntryPoint(messageService);
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new RosalindAccessDeniedHandler(messageService);
  }

}
