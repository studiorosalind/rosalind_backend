package com.rosalind.common.response.error;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RosalindErrorResponse {
  private String code;
  private String message;
  private Object data;

  public static RosalindErrorResponse from(String message, Object data) {
    return RosalindErrorResponse.builder()
      .message(message)
      .data(data)
      .build();
  }

}
