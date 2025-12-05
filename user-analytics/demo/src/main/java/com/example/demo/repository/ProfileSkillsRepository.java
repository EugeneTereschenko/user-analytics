package com.example.demo.repository;

import com.example.demo.model.ProfileSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileSkillsRepository extends JpaRepository<ProfileSkills, Long> {
    // Additional query methods can be defined here if needed
}
