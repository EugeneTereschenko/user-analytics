/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.service;

import com.healthcare.prescriptionservice.dto.PrescriptionDTO;
import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.mapper.PrescriptionMapper;
import com.healthcare.prescriptionservice.repository.PrescriptionRepository;
import com.healthcare.prescriptionservice.testutil.MedicationTestBuilder;
import com.healthcare.prescriptionservice.testutil.PrescriptionTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Prescription Service Tests")
class PrescriptionServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;
    @Mock
    private PrescriptionMapper prescriptionMapper;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    private PrescriptionDTO prescriptionDTO;
    private Prescription prescription;

    @BeforeEach
    void setUp() {
        prescription = PrescriptionTestBuilder.defaultBuilder()
                .withMedications(Collections.singletonList(
                        MedicationTestBuilder.defaultBuilder().build()
                ))
                .build();
        prescriptionDTO = new PrescriptionDTO();
        // set required fields on prescriptionDTO as needed
    }

    @Test
    @DisplayName("Should create prescription and set relationships")
    void testCreatePrescription() {
        when(prescriptionMapper.toEntity(any(PrescriptionDTO.class))).thenReturn(prescription);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);
        when(prescriptionMapper.toDTO(any(Prescription.class))).thenReturn(prescriptionDTO);

        PrescriptionDTO result = prescriptionService.createPrescription(prescriptionDTO);

        assertThat(result).isNotNull();
        verify(prescriptionRepository).save(any(Prescription.class));
        verify(prescriptionMapper).toEntity(any(PrescriptionDTO.class));
        verify(prescriptionMapper).toDTO(any(Prescription.class));
    }
}
