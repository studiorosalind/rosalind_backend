package com.rosalind.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RosalindUserRepository extends JpaRepository<RosalindUser, Long> {
    Optional<RosalindUser> findByServiceTypeAndProviderCodeAndProviderId(
            RosalindServiceType serviceType, 
            ProviderCode providerCode, 
            String providerId);
}
