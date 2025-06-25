package com.example.demo.repository;

import com.example.demo.model.ProfileProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileProjectRepository extends JpaRepository<ProfileProject, Long> {
    // Additional query methods can be defined here if needed
}
