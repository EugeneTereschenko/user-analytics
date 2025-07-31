package com.example.demo.repository;

import com.example.demo.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
   Optional<Audit> findByUser(String user);

}
