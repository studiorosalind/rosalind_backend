package com.rosalind.configuration.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RosalindUserPrincipal {

  private String name;
}
