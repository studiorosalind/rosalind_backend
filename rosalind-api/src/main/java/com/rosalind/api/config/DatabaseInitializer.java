package com.rosalind.api.config;

import com.rosalind.common.domain.ERole;
import com.rosalind.common.domain.Role;
import com.rosalind.common.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initRoles();
    }
    
    private void initRoles() {
        if (roleRepository.count() == 0) {
            log.info("Initializing roles in the database");
            
            Role userRole = new Role(ERole.ROLE_USER);
            Role modRole = new Role(ERole.ROLE_MODERATOR);
            Role adminRole = new Role(ERole.ROLE_ADMIN);
            
            roleRepository.save(userRole);
            roleRepository.save(modRole);
            roleRepository.save(adminRole);
            
            log.info("Roles initialized successfully");
        } else {
            log.info("Roles already exist in the database");
        }
    }
}
