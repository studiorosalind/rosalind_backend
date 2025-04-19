package com.rosalind.common.constant;

public final class Constants {


  public static final String[] publicPathAntPatterns = {
    "/error**",
    "/hello/**",
    "/actuator/**",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/static/index.html",
    "/index.html", "/favicon.ico",
    "/oauth2/**"
  };

  public static final class Auth {
    public static final long ACCESS_TOKEN_TTL_MILLISECOND = 300000L; // 5 minutes
    public static final long REFRESH_TOKEN_TTL_MILLISECOND = 10368000000L; // 120 Days
  }
}
