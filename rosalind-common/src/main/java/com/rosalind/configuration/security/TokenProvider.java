package com.rosalind.configuration.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
  private static final String GRANT_TYPE = "Bearer";

  private final Key rosalindJwtKey;

  public String validateToken(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(this.rosalindJwtKey).build().parseClaimsJws(token).getBody()
        .get("id").toString();
    } catch (MalformedJwtException | SecurityException var3) {
      log.error("wrong JWT signature.");
    } catch (ExpiredJwtException var4) {
      log.error("expired JWT token.");
    } catch (UnsupportedJwtException var5) {
      log.error("unsupported JWT.");
    } catch (IllegalArgumentException var6) {
      log.error("wrong JWT token.");
    }

    return null;

  }
}
