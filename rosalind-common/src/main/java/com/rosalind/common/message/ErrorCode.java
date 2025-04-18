package com.rosalind.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  GLOBAL_EXCEPTION(500, "요청한 작업을 처리하던 중 서버에서 에러가 발생했습니다.", "internal_server_error"),
  NOT_FOUND_API(404, "요청하신 API를 찾을 수 없습니다.", "not_found_api"),
  UNAUTHORIZED_ACCESS(401, "인증에 실패했습니다", "unauthorized"),
  FORBIDDEN(403, "금지된 접근입니다.", "forbidden");

  private final int status;
  private final String exceptionMessage;
  private final String messageKey;

  public static ErrorCode of(String name) {
    return ErrorCode.valueOf(name.toUpperCase());
  }
}
