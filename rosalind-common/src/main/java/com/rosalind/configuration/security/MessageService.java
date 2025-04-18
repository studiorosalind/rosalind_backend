package com.rosalind.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageSource messageSource;

  public Locale getCurrentLocale() {
    return LocaleContextHolder.getLocale();
  }

  public String getErrorMessage(String errorMessageKey) {
    return messageSource.getMessage(errorMessageKey, null, getCurrentLocale());
  }
}
