package com.example.authservice.service;

import com.example.authservice.model.Permission;
import com.example.authservice.model.Role;
import com.example.authservice.repository.PermissionRepository;
import com.example.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing database with default roles and permissions...");

        initializePermissions();
        initializeRoles();

        log.info("Database initialization completed");
    }

    @Transactional
    protected void initializePermissions() {
        List<PermissionData> permissionsData = Arrays.asList(
                // Patient permissions
                new PermissionData("PATIENT_CREATE", "Create patient records", "PATIENT", "CREATE"),
                new PermissionData("PATIENT_READ", "Read patient records", "PATIENT", "READ"),
                new PermissionData("PATIENT_UPDATE", "Update patient records", "PATIENT", "UPDATE"),
                new PermissionData("PATIENT_DELETE", "Delete patient records", "PATIENT", "DELETE"),

                // Appointment permissions
                new PermissionData("APPOINTMENT_CREATE", "Create appointments", "APPOINTMENT", "CREATE"),
                new PermissionData("APPOINTMENT_READ", "Read appointments", "APPOINTMENT", "READ"),
                new PermissionData("APPOINTMENT_UPDATE", "Update appointments", "APPOINTMENT", "UPDATE"),
                new PermissionData("APPOINTMENT_DELETE", "Delete appointments", "APPOINTMENT", "DELETE"),

                // Medical Record permissions
                new PermissionData("MEDICAL_RECORD_CREATE", "Create medical records", "MEDICAL_RECORD", "CREATE"),
                new PermissionData("MEDICAL_RECORD_READ", "Read medical records", "MEDICAL_RECORD", "READ"),
                new PermissionData("MEDICAL_RECORD_UPDATE", "Update medical records", "MEDICAL_RECORD", "UPDATE"),
                new PermissionData("MEDICAL_RECORD_DELETE", "Delete medical records", "MEDICAL_RECORD", "DELETE"),

                // Prescription permissions
                new PermissionData("PRESCRIPTION_CREATE", "Create prescriptions", "PRESCRIPTION", "CREATE"),
                new PermissionData("PRESCRIPTION_READ", "Read prescriptions", "PRESCRIPTION", "READ"),
                new PermissionData("PRESCRIPTION_UPDATE", "Update prescriptions", "PRESCRIPTION", "UPDATE"),
                new PermissionData("PRESCRIPTION_DELETE", "Delete prescriptions", "PRESCRIPTION", "DELETE"),

                // Billing permissions
                new PermissionData("BILLING_CREATE", "Create bills", "BILLING", "CREATE"),
                new PermissionData("BILLING_READ", "Read bills", "BILLING", "READ"),
                new PermissionData("BILLING_UPDATE", "Update bills", "BILLING", "UPDATE"),
                new PermissionData("BILLING_DELETE", "Delete bills", "BILLING", "DELETE"),

                // User management permissions
                new PermissionData("USER_CREATE", "Create users", "USER", "CREATE"),
                new PermissionData("USER_READ", "Read users", "USER", "READ"),
                new PermissionData("USER_UPDATE", "Update users", "USER", "UPDATE"),
                new PermissionData("USER_DELETE", "Delete users", "USER", "DELETE")
        );

        for (PermissionData data : permissionsData) {
            if (!permissionRepository.existsByName(data.name)) {
                Permission permission = Permission.builder()
                        .name(data.name)
                        .description(data.description)
                        .resource(data.resource)
                        .action(data.action)
                        .build();
                permissionRepository.save(permission);
                log.debug("Created permission: {}", data.name);
            }
        }
    }

    @Transactional
    protected void initializeRoles() {
        // Admin role with all permissions
        Set<String> allPermissionNames = permissionRepository.findAll()
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
        createRoleIfNotExists("ROLE_ADMIN", "Administrator with full access", allPermissionNames);

        // Doctor role
        createRoleIfNotExists("ROLE_DOCTOR", "Doctor with medical permissions",
                Set.of(
                        "PATIENT_READ", "PATIENT_UPDATE",
                        "APPOINTMENT_READ", "APPOINTMENT_UPDATE",
                        "MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ", "MEDICAL_RECORD_UPDATE",
                        "PRESCRIPTION_CREATE", "PRESCRIPTION_READ", "PRESCRIPTION_UPDATE"
                ));

        // Patient role
        createRoleIfNotExists("ROLE_PATIENT", "Patient with limited permissions",
                Set.of(
                        "APPOINTMENT_CREATE", "APPOINTMENT_READ",
                        "MEDICAL_RECORD_READ",
                        "PRESCRIPTION_READ",
                        "BILLING_READ"
                ));

        // Staff role
        createRoleIfNotExists("ROLE_STAFF", "Staff with operational permissions",
                Set.of(
                        "PATIENT_CREATE", "PATIENT_READ", "PATIENT_UPDATE",
                        "APPOINTMENT_CREATE", "APPOINTMENT_READ", "APPOINTMENT_UPDATE",
                        "BILLING_CREATE", "BILLING_READ", "BILLING_UPDATE"
                ));

        // Pharmacist role
        createRoleIfNotExists("ROLE_PHARMACIST", "Pharmacist with prescription permissions",
                Set.of(
                        "PRESCRIPTION_READ", "PRESCRIPTION_UPDATE"
                ));

        // Receptionist role
        createRoleIfNotExists("ROLE_RECEPTIONIST", "Receptionist with front desk permissions",
                Set.of(
                        "PATIENT_CREATE", "PATIENT_READ",
                        "APPOINTMENT_CREATE", "APPOINTMENT_READ", "APPOINTMENT_UPDATE"
                ));
    }

    private void createRoleIfNotExists(String name, String description, Set<String> permissionNames) {
        if (!roleRepository.existsByName(name)) {
            Set<Permission> permissions = permissionRepository.findByNameIn(permissionNames);

            Role role = Role.builder()
                    .name(name)
                    .description(description)
                    .permissions(new HashSet<>(permissions))
                    .build();
            roleRepository.save(role);
            log.info("Created role: {} with {} permissions", name, permissions.size());
        } else {
            log.debug("Role already exists: {}", name);
        }
    }

    private record PermissionData(String name, String description, String resource, String action) {}
}