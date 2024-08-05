package com.api.services;

import com.api.entities.Role;
import com.api.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER");
    }
}
