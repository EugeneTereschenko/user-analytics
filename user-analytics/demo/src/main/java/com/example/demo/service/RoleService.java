package com.example.demo.service;

import com.example.demo.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public void createRole(String roleName) {
        if (roleRepository.findByNameIn(java.util.Collections.singletonList(roleName)).isEmpty()) {
            roleRepository.save(new com.example.demo.model.Role(roleName));
        }
    }
}
