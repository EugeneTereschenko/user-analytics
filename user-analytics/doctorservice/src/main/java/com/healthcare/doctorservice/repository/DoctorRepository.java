package com.healthcare.doctorservice.repository;

import com.healthcare.doctorservice.entity.Doctor;
import com.healthcare.doctorservice.entity.DoctorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    boolean existsByEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);

    Page<Doctor> findBySpecialization(String specialization, Pageable pageable);

    Page<Doctor> findByStatus(DoctorStatus status, Pageable pageable);

    Page<Doctor> findByDepartment(String department, Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE d.status = :status AND d.specialization = :specialization")
    List<Doctor> findByStatusAndSpecialization(
            @Param("status") DoctorStatus status,
            @Param("specialization") String specialization
    );

    @Query("SELECT d FROM Doctor d WHERE d.status = 'ACTIVE'")
    List<Doctor> findAllActiveDoctors();

    @Query("SELECT d FROM Doctor d WHERE " +
            "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(d.department) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Doctor> searchDoctors(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT DISTINCT d.specialization FROM Doctor d WHERE d.status = 'ACTIVE' ORDER BY d.specialization")
    List<String> findAllSpecializations();

    @Query("SELECT DISTINCT d.department FROM Doctor d WHERE d.status = 'ACTIVE' ORDER BY d.department")
    List<String> findAllDepartments();

    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.status = 'ACTIVE' AND d.specialization = :specialization")
    Long countActiveBySpecialization(@Param("specialization") String specialization);
}
