package com.rosalind.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RosalindUserRepository extends JpaRepository<RosalindUser, Long> {
}
