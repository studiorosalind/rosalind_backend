package com.rosalind.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosalind.configuration.security.TokenProvider;
import com.rosalind.domain.user.ProviderCode;
import com.rosalind.domain.user.RosalindServiceType;
import com.rosalind.domain.user.RosalindUser;
import com.rosalind.domain.user.RosalindUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

  private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
  private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
  private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
  private static final String GOOGLE_CLIENT_ID = "301970471696-1cmcjem1vu5c87ktmrva224s74us7mae.apps.googleusercontent.com";
  private static final String GOOGLE_CLIENT_SECRE = "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";
  private static final String REDIRECT_URI_COOKIE_NAME = "oauth2_redirect_uri";
  private static final String ORIGINAL_URL_COOKIE_NAME = "oauth2_original_url";

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  private final RosalindUserService rosalindUserService;

  private final TokenProvider tokenProvider;

  @GetMapping("/oauth2/{serviceType}/{provider}/redirect")
  public void redirectToProvider(
    @PathVariable("serviceType") String serviceType,
    @PathVariable("provider") String provider,
    @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException {
    
    try {
      // Validate and convert serviceType and provider to enums
      RosalindServiceType serviceTypeEnum = RosalindServiceType.valueOf(serviceType.toUpperCase());
      ProviderCode providerCodeEnum = ProviderCode.valueOf(provider.toUpperCase());
      
      // Store the original URL for later redirect
      String originalUrl = redirectUrl != null ? redirectUrl : String.format("http://localhost:3000/%s/auth/success", serviceType.toLowerCase());
      
      // Store the redirect URI in a cookie
      String redirectUri = buildRedirectUri(serviceType.toLowerCase(), provider.toLowerCase());
      
      // Set cookies for later use
      Cookie redirectUriCookie = new Cookie(REDIRECT_URI_COOKIE_NAME, redirectUri);
      redirectUriCookie.setPath("/");
      redirectUriCookie.setMaxAge(3600); // 1 hour
      response.addCookie(redirectUriCookie);
      
      Cookie originalUrlCookie = new Cookie(ORIGINAL_URL_COOKIE_NAME, originalUrl);
      originalUrlCookie.setPath("/");
      originalUrlCookie.setMaxAge(3600); // 1 hour
      response.addCookie(originalUrlCookie);
      
      // Build the Google OAuth2 authorization URL
      String authorizationUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_AUTH_URL)
        .queryParam("client_id", GOOGLE_CLIENT_ID)
        .queryParam("redirect_uri", redirectUri)
        .queryParam("response_type", "code")
        .queryParam("scope", "email profile openid")
        .queryParam("access_type", "offline")
        .queryParam("prompt", "consent")
        .build().toUriString();
      
      // Redirect to Google's authorization URL
      response.sendRedirect(authorizationUrl);
    } catch (IllegalArgumentException e) {
      log.error("Invalid service type or provider: {}", e.getMessage());
      response.sendRedirect("/error?message=Invalid service type or provider");
    }
  }
  
  @GetMapping("/oauth2/{serviceType}/{provider}/callback")
  public void handleCallback(
    @PathVariable("serviceType") String serviceType,
    @PathVariable("provider") String provider,
    @RequestParam("code") String code,
    HttpServletRequest request,
    HttpServletResponse response) throws IOException {
    
    try {
      // Validate and convert serviceType and provider to enums
      RosalindServiceType serviceTypeEnum = RosalindServiceType.valueOf(serviceType.toUpperCase());
      ProviderCode providerCodeEnum = ProviderCode.valueOf(provider.toUpperCase());
      
      // Get the redirect URI from the cookie
      String redirectUri = getRedirectUriFromCookie(request);
      if (redirectUri == null) {
        redirectUri = buildRedirectUri(serviceType, provider);
      }
      
      // Get the original URL from the cookie
      String originalUrl = getOriginalUrlFromCookie(request);
      if (originalUrl == null) {
        originalUrl = String.format("http://localhost:3000/%s/auth/success", serviceType.toLowerCase());
      }
      
      // Exchange the authorization code for an access token
      String accessToken = getGoogleAccessToken(code, redirectUri);
      
      // Get the user information from Google
      JsonNode userInfo = getGoogleUserInfo(accessToken);
      
      // Extract user details
      String googleId = userInfo.get("sub").asText();
      String email = userInfo.has("email") ? userInfo.get("email").asText() : "";
      String name = userInfo.has("name") ? userInfo.get("name").asText() : "";
      String pictureUrl = userInfo.has("picture") ? userInfo.get("picture").asText() : "";
      
      // Create or update the user in the database
      RosalindUser user = rosalindUserService.createOrUpdateOAuthUser(
        serviceTypeEnum,
        providerCodeEnum,
        googleId,
        name,
        email,
        pictureUrl
      );
      
      // Generate JWT tokens
      String jwtAccessToken = tokenProvider.generateAccessToken(user);
      String jwtRefreshToken = tokenProvider.generateRefreshToken(user);
      
      // Build the redirect URL with tokens
      String redirectUrl = UriComponentsBuilder.fromUriString(originalUrl)
        .queryParam("accessToken", jwtAccessToken)
        .queryParam("refreshToken", jwtRefreshToken)
        .build().toUriString();
      
      // Clear the cookies
      clearCookies(response);
      
      // Redirect to the frontend with tokens
      response.sendRedirect(redirectUrl);
    } catch (Exception e) {
      log.error("Error during OAuth callback: {}", e.getMessage());
      response.sendRedirect("/error?message=Authentication failed");
    }
  }
  
  private String buildRedirectUri(String serviceType, String provider) {
    return String.format("http://localhost:8080/oauth2/%s/%s/callback", serviceType, provider);
  }
  
  private String getRedirectUriFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      Optional<Cookie> cookie = Arrays.stream(cookies)
        .filter(c -> REDIRECT_URI_COOKIE_NAME.equals(c.getName()))
        .findFirst();
      
      return cookie.map(Cookie::getValue).orElse(null);
    }
    return null;
  }
  
  private String getOriginalUrlFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      Optional<Cookie> cookie = Arrays.stream(cookies)
        .filter(c -> ORIGINAL_URL_COOKIE_NAME.equals(c.getName()))
        .findFirst();
      
      return cookie.map(Cookie::getValue).orElse(null);
    }
    return null;
  }
  
  private void clearCookies(HttpServletResponse response) {
    Cookie redirectUriCookie = new Cookie(REDIRECT_URI_COOKIE_NAME, "");
    redirectUriCookie.setPath("/");
    redirectUriCookie.setMaxAge(0);
    response.addCookie(redirectUriCookie);
    
    Cookie originalUrlCookie = new Cookie(ORIGINAL_URL_COOKIE_NAME, "");
    originalUrlCookie.setPath("/");
    originalUrlCookie.setMaxAge(0);
    response.addCookie(originalUrlCookie);
  }
  
  private String getGoogleAccessToken(String code, String redirectUri) throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("code", code);
    body.add("client_id", GOOGLE_CLIENT_ID);
    body.add("client_secret", GOOGLE_CLIENT_SECRE);
    body.add("redirect_uri", redirectUri);
    body.add("grant_type", "authorization_code");
    
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
    
    String response = restTemplate.exchange(
      GOOGLE_TOKEN_URL,
      HttpMethod.POST,
      entity,
      String.class
    ).getBody();
    
    JsonNode node = objectMapper.readTree(response);
    return node.get("access_token").asText();
  }
  
  private JsonNode getGoogleUserInfo(String accessToken) throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    
    HttpEntity<String> entity = new HttpEntity<>("", headers);
    
    String response = restTemplate.exchange(
      GOOGLE_USERINFO_URL,
      HttpMethod.GET,
      entity,
      String.class
    ).getBody();
    
    return objectMapper.readTree(response);
  }
}
