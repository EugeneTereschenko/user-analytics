/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.prescriptionservice.PrescriptionServiceApplication;
import com.healthcare.prescriptionservice.dto.MedicationDTO;
import com.healthcare.prescriptionservice.dto.PrescriptionDTO;
import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.testutil.MedicationTestBuilder;
import com.healthcare.prescriptionservice.testutil.PrescriptionTestBuilder;
import com.healthcare.prescriptionservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {PrescriptionServiceApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Should create prescription via POST /prescriptions")
    void shouldCreatePrescription() throws Exception {
        // Build test data
        Prescription prescription = PrescriptionTestBuilder.defaultBuilder()
                .withMedications(Collections.singletonList(
                        MedicationTestBuilder.defaultBuilder().build()
                ))
                .build();

        // Map to DTO (populate all required fields)

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setPrescriptionNumber(prescription.getPrescriptionNumber());
        prescriptionDTO.setPatientId(prescription.getPatientId());
        prescriptionDTO.setDoctorId(prescription.getDoctorId());
        prescriptionDTO.setPrescriptionDate(prescription.getPrescriptionDate());
// Set all other required fields...
        prescriptionDTO.setMedications(
                prescription.getMedications().stream()
                        .map(med -> {
                            MedicationDTO medDTO = new MedicationDTO();
                            medDTO.setMedicationName(med.getMedicationName());
                            medDTO.setGenericName(med.getGenericName());
                            medDTO.setDrugCode(med.getDrugCode());
                            medDTO.setStrength(med.getStrength());
                            medDTO.setDosage(med.getDosage());
                            medDTO.setDosageForm(med.getDosageForm());
                            medDTO.setFrequency(med.getFrequency());
                            medDTO.setDuration(med.getDuration());
                            medDTO.setQuantity(med.getQuantity());
                            medDTO.setUnit(med.getUnit());
                            medDTO.setRoute(med.getRoute());
                            medDTO.setInstructions(med.getInstructions());
                            medDTO.setStartDate(med.getStartDate());
                            medDTO.setEndDate(med.getEndDate());
                            medDTO.setWarnings(med.getWarnings());
                            medDTO.setSideEffects(med.getSideEffects());
                            medDTO.setIsControlledSubstance(med.getIsControlledSubstance());
                            medDTO.setIsGenericAllowed(med.getIsGenericAllowed());
                            medDTO.setPriority(med.getPriority());
                            // Set all other required fields...
                            return medDTO;
                        })
                        .collect(Collectors.toList())
        );
// Set all other required fields for prescriptionDTO...

        // Set other required fields...

        mockMvc.perform(post("/api/v1/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prescriptionDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

}
