package com.healthcare.doctorservice.service;

import com.healthcare.doctorservice.dto.DoctorDTO;
import com.healthcare.doctorservice.entity.Doctor;
import com.healthcare.doctorservice.entity.DoctorStatus;
import com.healthcare.doctorservice.exception.DoctorAlreadyExistsException;
import com.healthcare.doctorservice.exception.DoctorNotFoundException;
import com.healthcare.doctorservice.mapper.DoctorMapper;
import com.healthcare.doctorservice.repository.DoctorRepository;
import com.healthcare.doctorservice.service.DoctorServiceImpl;
import com.healthcare.doctorservice.testutil.DoctorTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Doctor Service Tests")
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private DoctorDTO doctorDTO;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctorDTO = new DoctorDTO();
        doctorDTO.setEmail("test@doc.com");
        doctorDTO.setLicenseNumber("LIC123");

        doctor = DoctorTestBuilder.aDoctor()
                .withFirstName("Alice")
                .withLastName("Smith")
                .withEmail("test@doc.com")
                .withLicenseNumber("LIC123")
                .build();
    }


    @Test
    void createDoctor_success() {
        when(doctorRepository.existsByEmail(anyString())).thenReturn(false);
        when(doctorRepository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(doctorMapper.toEntity(any(DoctorDTO.class))).thenReturn(doctor);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(doctorMapper.toDTO(any(Doctor.class))).thenReturn(doctorDTO);

        DoctorDTO result = doctorService.createDoctor(doctorDTO);

        assertNotNull(result);
        assertEquals(doctorDTO.getEmail(), result.getEmail());
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void createDoctor_emailExists_throwsException() {
        when(doctorRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DoctorAlreadyExistsException.class, () -> doctorService.createDoctor(doctorDTO));
    }

    @Test
    void createDoctor_licenseExists_throwsException() {
        when(doctorRepository.existsByEmail(anyString())).thenReturn(false);
        when(doctorRepository.existsByLicenseNumber(anyString())).thenReturn(true);

        assertThrows(DoctorAlreadyExistsException.class, () -> doctorService.createDoctor(doctorDTO));
    }

    @Test
    void getDoctorById_success() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDTO(any(Doctor.class))).thenReturn(doctorDTO);

        DoctorDTO result = doctorService.getDoctorById(1L);

        assertNotNull(result);
        assertEquals(doctorDTO.getEmail(), result.getEmail());
    }

    @Test
    void getDoctorById_notFound_throwsException() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.getDoctorById(1L));
    }
}
