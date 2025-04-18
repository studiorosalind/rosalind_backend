package com.rosalind.common.exception;

import java.util.function.Supplier;

public class RosalindException extends RuntimeException implements Supplier<RuntimeException> {
  public RosalindException() {
    super();
  }

  public RosalindException(String message) {
    super(message);
  }

  public RosalindException(String message, Throwable e) {
    super(message, e);
  }

  public RosalindException(String message, Object... args) {
    super(String.format(message, args));
  }

  @Override
  public RuntimeException get() {
    return this;
  }

}
