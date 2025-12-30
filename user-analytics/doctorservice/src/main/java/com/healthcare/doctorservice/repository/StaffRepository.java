package com.healthcare.doctorservice.repository;

import com.healthcare.doctorservice.entity.Staff;
import com.healthcare.doctorservice.entity.StaffRole;
import com.healthcare.doctorservice.entity.StaffStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByEmail(String email);

    Optional<Staff> findByEmployeeId(String employeeId);

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    Page<Staff> findByRole(StaffRole role, Pageable pageable);

    Page<Staff> findByStatus(StaffStatus status, Pageable pageable);

    Page<Staff> findByDepartment(String department, Pageable pageable);

    List<Staff> findBySupervisorId(Long supervisorId);

    @Query("SELECT s FROM Staff s WHERE s.status = 'ACTIVE'")
    List<Staff> findAllActiveStaff();

    @Query("SELECT s FROM Staff s WHERE s.status = :status AND s.role = :role")
    List<Staff> findByStatusAndRole(
            @Param("status") StaffStatus status,
            @Param("role") StaffRole role
    );

    @Query("SELECT s FROM Staff s WHERE " +
            "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.department) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.employeeId) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Staff> searchStaff(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Staff s WHERE s.status = 'ACTIVE' AND s.role = :role")
    Long countActiveByRole(@Param("role") StaffRole role);

    @Query("SELECT COUNT(s) FROM Staff s WHERE s.status = 'ACTIVE' AND s.department = :department")
    Long countActiveByDepartment(@Param("department") String department);
}