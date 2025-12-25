/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.repository;

import com.healthcare.patientservice.entity.Patient;
import com.healthcare.patientservice.entity.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT p FROM Patient p WHERE p.status = :status")
    Page<Patient> findByStatus(@Param("status") PatientStatus status, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Patient> searchPatients(@Param("searchTerm") String searchTerm, Pageable pageable);
}
