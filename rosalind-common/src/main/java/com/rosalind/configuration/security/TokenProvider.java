package com.rosalind.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {
  private static final String GRANT_TYPE = "Bearer";


}
