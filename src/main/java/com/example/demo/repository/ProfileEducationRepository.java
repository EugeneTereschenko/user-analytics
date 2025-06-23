package com.example.demo.repository;

import com.example.demo.model.ProfileEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileEducationRepository extends JpaRepository<ProfileEducation, Long> {
}
