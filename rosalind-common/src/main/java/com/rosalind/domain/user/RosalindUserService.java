package com.rosalind.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RosalindUserService {

  private final RosalindUserRepository rosalindUserRepository;

  @Transactional(readOnly=true)
  public RosalindUser getRosalindUser(Long rosalindUserId) {
    return rosalindUserRepository.findById(rosalindUserId)
      .orElse(null);
  }
  
  @Transactional(readOnly=true)
  public Optional<RosalindUser> findByProviderInfo(RosalindServiceType serviceType, ProviderCode providerCode, String providerId) {
    return rosalindUserRepository.findByServiceTypeAndProviderCodeAndProviderId(serviceType, providerCode, providerId);
  }
  
  @Transactional
  public RosalindUser createOrUpdateOAuthUser(
      RosalindServiceType serviceType, 
      ProviderCode providerCode, 
      String providerId, 
      String username, 
      String email, 
      String profileUrl) {
    
    Optional<RosalindUser> existingUser = findByProviderInfo(serviceType, providerCode, providerId);
    
    if (existingUser.isPresent()) {
      RosalindUser user = existingUser.get();
      user.setUsername(username);
      user.setEmail(email);
      user.setProfileUrl(profileUrl);
      return rosalindUserRepository.save(user);
    } else {
      RosalindUser newUser = RosalindUser.builder()
          .serviceType(serviceType)
          .providerCode(providerCode)
          .providerId(providerId)
          .username(username)
          .email(email)
          .profileUrl(profileUrl)
          .userUuid(UUID.randomUUID().toString())
          .build();
      
      return rosalindUserRepository.save(newUser);
    }
  }
}
