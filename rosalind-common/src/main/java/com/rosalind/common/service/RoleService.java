package com.rosalind.common.service;

import com.rosalind.common.domain.ERole;
import com.rosalind.common.domain.Role;
import com.rosalind.common.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    
    private final RoleRepository roleRepository;
    
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Role> findByName(ERole name) {
        return roleRepository.findByName(name);
    }
    
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
