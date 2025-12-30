/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.doctorservice.service.impl;

import com.healthcare.doctorservice.dto.DoctorDTO;
import com.healthcare.doctorservice.entity.DoctorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);

    @Transactional(readOnly = true)
    DoctorDTO getDoctorById(Long id);

    @Transactional(readOnly = true)
    DoctorDTO getDoctorByEmail(String email);

    @Transactional(readOnly = true)
    DoctorDTO getDoctorByLicenseNumber(String licenseNumber);

    @Transactional(readOnly = true)
    Page<DoctorDTO> getAllDoctors(Pageable pageable);

    @Transactional(readOnly = true)
    Page<DoctorDTO> getDoctorsBySpecialization(String specialization, Pageable pageable);

    @Transactional(readOnly = true)
    Page<DoctorDTO> getDoctorsByStatus(DoctorStatus status, Pageable pageable);

    @Transactional(readOnly = true)
    Page<DoctorDTO> getDoctorsByDepartment(String department, Pageable pageable);

    @Transactional(readOnly = true)
    List<DoctorDTO> getActiveDoctors();

    @Transactional(readOnly = true)
    Page<DoctorDTO> searchDoctors(String searchTerm, Pageable pageable);

    @Transactional(readOnly = true)
    List<String> getAllSpecializations();

    @Transactional(readOnly = true)
    List<String> getAllDepartments();

    DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO);

    DoctorDTO updateDoctorStatus(Long id, DoctorStatus status);

    void deleteDoctor(Long id);
}
