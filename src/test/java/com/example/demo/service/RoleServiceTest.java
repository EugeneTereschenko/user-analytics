package com.example.demo.service;
import com.example.demo.repository.RoleRepository;
import com.example.demo.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRole() {
        String roleName = "ROLE_USER";

        // Mock behavior for roleRepository
        when(roleRepository.findByNameIn(java.util.Collections.singletonList(roleName)))
                .thenReturn(java.util.Collections.emptyList());

        // Call the method under test
        roleService.createRole(roleName);

        // Verify that save was called with the correct Role object
        verify(roleRepository, times(1)).save(new Role(roleName));
    }
}