/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.doctorservice.service;

import com.healthcare.doctorservice.dto.StaffDTO;
import com.healthcare.doctorservice.entity.Staff;
import com.healthcare.doctorservice.entity.StaffRole;
import com.healthcare.doctorservice.entity.StaffStatus;
import com.healthcare.doctorservice.exception.StaffAlreadyExistsException;
import com.healthcare.doctorservice.exception.StaffNotFoundException;
import com.healthcare.doctorservice.mapper.StaffMapper;
import com.healthcare.doctorservice.repository.StaffRepository;
import com.healthcare.doctorservice.service.impl.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;

    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) {
        log.info("Creating staff with email: {}", staffDTO.getEmail());

        if (staffRepository.existsByEmail(staffDTO.getEmail())) {
            throw new StaffAlreadyExistsException(
                    "Staff with email " + staffDTO.getEmail() + " already exists");
        }

        if (staffDTO.getEmployeeId() != null &&
                staffRepository.existsByEmployeeId(staffDTO.getEmployeeId())) {
            throw new StaffAlreadyExistsException(
                    "Staff with employee ID " + staffDTO.getEmployeeId() + " already exists");
        }

        staffDTO.checkUserId();

        Staff staff = staffMapper.toEntity(staffDTO);
        Staff savedStaff = staffRepository.save(staff);

        log.info("Staff created successfully with ID: {}", savedStaff.getId());
        return staffMapper.toDTO(savedStaff);
    }

    @Transactional(readOnly = true)
    @Override
    public StaffDTO getStaffById(Long id) {
        log.info("Fetching staff with ID: {}", id);

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException(
                        "Staff not found with ID: " + id));

        return staffMapper.toDTO(staff);
    }

    @Transactional(readOnly = true)
    @Override
    public StaffDTO getStaffByEmail(String email) {
        log.info("Fetching staff with email: {}", email);

        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new StaffNotFoundException(
                        "Staff not found with email: " + email));

        return staffMapper.toDTO(staff);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StaffDTO> getAllStaff(Pageable pageable) {
        log.info("Fetching all staff with pagination");
        return staffRepository.findAll(pageable)
                .map(staffMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StaffDTO> getStaffByRole(StaffRole role, Pageable pageable) {
        log.info("Fetching staff with role: {}", role);
        return staffRepository.findByRole(role, pageable)
                .map(staffMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StaffDTO> getStaffByStatus(StaffStatus status, Pageable pageable) {
        log.info("Fetching staff with status: {}", status);
        return staffRepository.findByStatus(status, pageable)
                .map(staffMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StaffDTO> getStaffByDepartment(String department, Pageable pageable) {
        log.info("Fetching staff in department: {}", department);
        return staffRepository.findByDepartment(department, pageable)
                .map(staffMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StaffDTO> getActiveStaff() {
        log.info("Fetching all active staff");
        return staffRepository.findAllActiveStaff()
                .stream()
                .map(staffMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StaffDTO> searchStaff(String searchTerm, Pageable pageable) {
        log.info("Searching staff with term: {}", searchTerm);
        return staffRepository.searchStaff(searchTerm, pageable)
                .map(staffMapper::toDTO);
    }

    @Override
    public StaffDTO updateStaff(Long id, StaffDTO staffDTO) {
        log.info("Updating staff with ID: {}", id);

        Staff existingStaff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException(
                        "Staff not found with ID: " + id));

        if (!existingStaff.getEmail().equals(staffDTO.getEmail()) &&
                staffRepository.existsByEmail(staffDTO.getEmail())) {
            throw new StaffAlreadyExistsException(
                    "Staff with email " + staffDTO.getEmail() + " already exists");
        }

        staffMapper.updateEntityFromDTO(staffDTO, existingStaff);
        Staff updatedStaff = staffRepository.save(existingStaff);

        log.info("Staff updated successfully with ID: {}", id);
        return staffMapper.toDTO(updatedStaff);
    }

    @Override
    public StaffDTO updateStaffStatus(Long id, StaffStatus status) {
        log.info("Updating staff {} status to {}", id, status);

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException(
                        "Staff not found with ID: " + id));

        staff.setStatus(status);
        Staff updatedStaff = staffRepository.save(staff);

        log.info("Staff status updated successfully");
        return staffMapper.toDTO(updatedStaff);
    }

    @Override
    public void deleteStaff(Long id) {
        log.info("Deleting staff with ID: {}", id);

        if (!staffRepository.existsById(id)) {
            throw new StaffNotFoundException("Staff not found with ID: " + id);
        }

        staffRepository.deleteById(id);
        log.info("Staff deleted successfully");
    }
}
