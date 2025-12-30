/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.doctorservice.service.impl;

import com.healthcare.doctorservice.dto.StaffDTO;
import com.healthcare.doctorservice.entity.StaffRole;
import com.healthcare.doctorservice.entity.StaffStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StaffService {
    StaffDTO createStaff(StaffDTO staffDTO);

    @Transactional(readOnly = true)
    StaffDTO getStaffById(Long id);

    @Transactional(readOnly = true)
    StaffDTO getStaffByEmail(String email);

    @Transactional(readOnly = true)
    Page<StaffDTO> getAllStaff(Pageable pageable);

    @Transactional(readOnly = true)
    Page<StaffDTO> getStaffByRole(StaffRole role, Pageable pageable);

    @Transactional(readOnly = true)
    Page<StaffDTO> getStaffByStatus(StaffStatus status, Pageable pageable);

    @Transactional(readOnly = true)
    Page<StaffDTO> getStaffByDepartment(String department, Pageable pageable);

    @Transactional(readOnly = true)
    List<StaffDTO> getActiveStaff();

    @Transactional(readOnly = true)
    Page<StaffDTO> searchStaff(String searchTerm, Pageable pageable);

    StaffDTO updateStaff(Long id, StaffDTO staffDTO);

    StaffDTO updateStaffStatus(Long id, StaffStatus status);

    void deleteStaff(Long id);
}
