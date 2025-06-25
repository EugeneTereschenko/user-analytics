package com.example.demo.repository;

import com.example.demo.model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {
    // Additional query methods can be defined here if needed
}
