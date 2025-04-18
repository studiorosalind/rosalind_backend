package com.rosalind.configuration.security;

import com.rosalind.domain.user.ProviderCode;
import com.rosalind.domain.user.RosalindServiceType;
import com.rosalind.domain.user.RosalindUser;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RosalindUserPrincipal {
  private Long rosalindUserId;
  private String userName;
  private String userUuid;
  private ProviderCode providerCode;
  private String providerId;
  private RosalindServiceType serviceType;

  public static RosalindUserPrincipal from(RosalindUser rosalindUser) {
    return RosalindUserPrincipal.builder()
      .rosalindUserId(rosalindUser.getRosalindUserId())
      .userName(rosalindUser.getUsername())
      .userUuid(rosalindUser.getUserUuid())
      .providerCode(rosalindUser.getProviderCode())
      .providerId(rosalindUser.getProviderId())
      .serviceType(rosalindUser.getServiceType())
      .build();
  }
}
