package com.example.demo.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.demo.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findTopByOrderByIdDesc();
}
