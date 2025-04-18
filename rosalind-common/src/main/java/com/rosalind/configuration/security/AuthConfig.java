package com.rosalind.configuration.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class AuthConfig {

  @Bean
  public Key rosalindJwtKey(@Value("${jwt.secret}") String secretKey) {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  @Bean
  public JwtParser rosalindJwtParser(Key sugarJwtKey) {
    return Jwts.parserBuilder()
      .setSigningKey(sugarJwtKey).build();
  }

}
