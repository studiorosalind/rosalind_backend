package com.rosalind.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class RosalindUserService {

  private final RosalindUserRepository rosalindUserRepository;

  @Transactional(readOnly=true)
  public RosalindUser getRosalindUser(Long rosalindUserId) {
    return rosalindUserRepository.findById(rosalindUserId)
      .orElse(null);
  }
}
