package com.rosalind.common.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  GLOBAL_EXCEPTION(500,
    "CMM1",
    "요청한 작업을 처리하던 중 서버에서 에러가 발생했습니다.",
    "global_exception"),
  NOT_FOUND_API(404,
    "CMM2",
    "요청하신 API를 찾을 수 없습니다.",
    "not_found_api");

  private final int status;
  private final String code;
  private final String exceptionMessage;
  private final String messageKey;

  }
