package com.healthcare.doctorservice.service;

import com.healthcare.doctorservice.dto.DoctorDTO;
import com.healthcare.doctorservice.entity.Doctor;
import com.healthcare.doctorservice.entity.DoctorStatus;
import com.healthcare.doctorservice.exception.DoctorAlreadyExistsException;
import com.healthcare.doctorservice.exception.DoctorNotFoundException;
import com.healthcare.doctorservice.mapper.DoctorMapper;
import com.healthcare.doctorservice.repository.DoctorRepository;
import com.healthcare.doctorservice.service.impl.DoctorService;
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
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        log.info("Creating doctor with email: {}", doctorDTO.getEmail());

        if (doctorRepository.existsByEmail(doctorDTO.getEmail())) {
            throw new DoctorAlreadyExistsException(
                    "Doctor with email " + doctorDTO.getEmail() + " already exists");
        }

        if (doctorDTO.getLicenseNumber() != null &&
                doctorRepository.existsByLicenseNumber(doctorDTO.getLicenseNumber())) {
            throw new DoctorAlreadyExistsException(
                    "Doctor with license number " + doctorDTO.getLicenseNumber() + " already exists");
        }

        doctorDTO.checkUserId();

        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        Doctor savedDoctor = doctorRepository.save(doctor);

        log.info("Doctor created successfully with ID: {}", savedDoctor.getId());
        return doctorMapper.toDTO(savedDoctor);
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorDTO getDoctorById(Long id) {
        log.info("Fetching doctor with ID: {}", id);

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with ID: " + id));

        return doctorMapper.toDTO(doctor);
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorDTO getDoctorByEmail(String email) {
        log.info("Fetching doctor with email: {}", email);

        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with email: " + email));

        return doctorMapper.toDTO(doctor);
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorDTO getDoctorByLicenseNumber(String licenseNumber) {
        log.info("Fetching doctor with license number: {}", licenseNumber);

        Doctor doctor = doctorRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with license number: " + licenseNumber));

        return doctorMapper.toDTO(doctor);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        log.info("Fetching all doctors with pagination");
        return doctorRepository.findAll(pageable)
                .map(doctorMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DoctorDTO> getDoctorsBySpecialization(String specialization, Pageable pageable) {
        log.info("Fetching doctors with specialization: {}", specialization);
        return doctorRepository.findBySpecialization(specialization, pageable)
                .map(doctorMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DoctorDTO> getDoctorsByStatus(DoctorStatus status, Pageable pageable) {
        log.info("Fetching doctors with status: {}", status);
        return doctorRepository.findByStatus(status, pageable)
                .map(doctorMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DoctorDTO> getDoctorsByDepartment(String department, Pageable pageable) {
        log.info("Fetching doctors in department: {}", department);
        return doctorRepository.findByDepartment(department, pageable)
                .map(doctorMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorDTO> getActiveDoctors() {
        log.info("Fetching all active doctors");
        return doctorRepository.findAllActiveDoctors()
                .stream()
                .map(doctorMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DoctorDTO> searchDoctors(String searchTerm, Pageable pageable) {
        log.info("Searching doctors with term: {}", searchTerm);
        return doctorRepository.searchDoctors(searchTerm, pageable)
                .map(doctorMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getAllSpecializations() {
        log.info("Fetching all specializations");
        return doctorRepository.findAllSpecializations();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getAllDepartments() {
        log.info("Fetching all departments");
        return doctorRepository.findAllDepartments();
    }

    @Override
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        log.info("Updating doctor with ID: {}", id);

        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with ID: " + id));

        if (!existingDoctor.getEmail().equals(doctorDTO.getEmail()) &&
                doctorRepository.existsByEmail(doctorDTO.getEmail())) {
            throw new DoctorAlreadyExistsException(
                    "Doctor with email " + doctorDTO.getEmail() + " already exists");
        }

        doctorMapper.updateEntityFromDTO(doctorDTO, existingDoctor);
        Doctor updatedDoctor = doctorRepository.save(existingDoctor);

        log.info("Doctor updated successfully with ID: {}", id);
        return doctorMapper.toDTO(updatedDoctor);
    }

    @Override
    public DoctorDTO updateDoctorStatus(Long id, DoctorStatus status) {
        log.info("Updating doctor {} status to {}", id, status);

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with ID: " + id));

        doctor.setStatus(status);
        Doctor updatedDoctor = doctorRepository.save(doctor);

        log.info("Doctor status updated successfully");
        return doctorMapper.toDTO(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with ID: {}", id);

        if (!doctorRepository.existsById(id)) {
            throw new DoctorNotFoundException("Doctor not found with ID: " + id);
        }

        doctorRepository.deleteById(id);
        log.info("Doctor deleted successfully");
    }
}
