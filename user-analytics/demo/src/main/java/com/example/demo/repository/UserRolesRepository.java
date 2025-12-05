package com.example.demo.repository;

import com.example.demo.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find roles by userId:
    // List<UserRoles> findByUserId(Long userId);
}
