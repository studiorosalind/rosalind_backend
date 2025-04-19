package com.rosalind.configuration.security;

import com.rosalind.common.constant.Constants;
import com.rosalind.domain.user.ProviderCode;
import com.rosalind.domain.user.RosalindServiceType;
import com.rosalind.domain.user.RosalindUser;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
  
  public String generateAccessToken(RosalindUser user) {
    return generateToken(user, Constants.Auth.ACCESS_TOKEN_TTL_MILLISECOND);
  }
  
  public String generateRefreshToken(RosalindUser user) {
    return generateToken(user, Constants.Auth.REFRESH_TOKEN_TTL_MILLISECOND);
  }
  
  private String generateToken(RosalindUser user, long expirationTime) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationTime);
    
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", user.getRosalindUserId());
    claims.put("serviceType", user.getServiceType().name());
    claims.put("providerCode", user.getProviderCode().name());
    
    return Jwts.builder()
      .setClaims(claims)
      .setSubject(user.getRosalindUserId().toString())
      .setIssuedAt(now)
      .setExpiration(expiryDate)
      .signWith(rosalindJwtKey)
      .compact();
  }
  
  public Claims parseToken(String token) {
    try {
      return Jwts.parserBuilder()
        .setSigningKey(rosalindJwtKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
    } catch (Exception e) {
      log.error("Error parsing token: {}", e.getMessage());
      return null;
    }
  }
  
  public boolean validateRefreshToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(this.rosalindJwtKey).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      log.error("Invalid refresh token: {}", e.getMessage());
      return false;
    }
  }
}
