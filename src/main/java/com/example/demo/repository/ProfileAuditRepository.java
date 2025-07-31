package com.example.demo.repository;

import com.example.demo.model.ProfileAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileAuditRepository extends JpaRepository<ProfileAudit, Long> {
}
