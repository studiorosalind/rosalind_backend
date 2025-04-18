package com.rosalind.configuration.web;

import com.rosalind.common.exception.RosalindException;
import com.rosalind.common.message.ErrorCode;
import com.rosalind.common.response.error.RosalindErrorResponse;
import com.rosalind.configuration.security.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.rosalind.common.message.ErrorCode.GLOBAL_EXCEPTION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageService messageService;

  @ExceptionHandler(value = { RosalindException.class })
  public final ResponseEntity<Object> handleBadRequest(RuntimeException ex) {
    log.warn("[{}] : {}", BAD_REQUEST.getReasonPhrase(), ex.getMessage());
    ErrorCode errorCode = ErrorCode.of(ex.getMessage());
    return new ResponseEntity<>(RosalindErrorResponse.from(messageService.getErrorMessage(
      errorCode.getMessageKey()
    ), ex.getMessage()), BAD_REQUEST);
  }

  @ExceptionHandler(value = { Exception.class })
  public final ResponseEntity<Object> handleInternalServerError(RuntimeException ex) {
    log.error("[{}] : {}", INTERNAL_SERVER_ERROR.getReasonPhrase(), getExceptionTrace(ex));
    return new ResponseEntity<>(RosalindErrorResponse.from(messageService.getErrorMessage(
      GLOBAL_EXCEPTION.getMessageKey()
    ), ex.getMessage()), INTERNAL_SERVER_ERROR);
  }

  private String getExceptionTrace(RuntimeException ex) {
    StringWriter sw = new StringWriter();
    ex.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  private String getErrorTrace(Throwable throwable) {
    StringWriter sw = new StringWriter();
    throwable.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

}
