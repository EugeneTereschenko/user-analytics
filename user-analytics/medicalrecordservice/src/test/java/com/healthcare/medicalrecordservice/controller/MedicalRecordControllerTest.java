/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.medicalrecordservice.MedicalRecordServiceApplication;
import com.healthcare.medicalrecordservice.config.WithMockUserPrincipal;
import com.healthcare.medicalrecordservice.dto.MedicalRecordDTO;
import com.healthcare.medicalrecordservice.entity.RecordStatus;
import com.healthcare.medicalrecordservice.entity.RecordType;
import com.healthcare.medicalrecordservice.repository.MedicalRecordRepository;
import com.healthcare.medicalrecordservice.testutil.MedicalRecordTestBuilder;
import com.healthcare.medicalrecordservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {MedicalRecordServiceApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUserPrincipal(
            username = "admin",
            roles = {"ROLE_ADMIN"},
            permissions = {"MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ"}
    )
    @DisplayName("Should create a medical record")
    void shouldCreateMedicalRecord() throws Exception {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(1L);
        dto.setDoctorId(2L);
        dto.setRecordType(RecordType.CONSULTATION);
        dto.setTitle("Test Title");
        dto.setRecordDate(java.time.LocalDate.now()); // Set required field

        mockMvc.perform(post("/api/v1/medical-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.doctorId").value(2L))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    @WithMockUserPrincipal(
            username = "admin",
            roles = {"ROLE_ADMIN"},
            permissions = {"MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ"}
    )
    @DisplayName("Should get medical record by id")
    void shouldGetMedicalRecordById() throws Exception {
        var entity = new MedicalRecordTestBuilder().withPatientId(10L).withDoctorId(20L).build();
        var saved = medicalRecordRepository.save(entity);

        mockMvc.perform(get("/api/v1/medical-records/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(10L))
                .andExpect(jsonPath("$.doctorId").value(20L));
    }

    @Test
    @WithMockUserPrincipal(
            username = "admin",
            roles = {"ROLE_ADMIN"},
            permissions = {"MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ"}
    )
    @DisplayName("Should get all medical records")
    void shouldGetAllMedicalRecords() throws Exception {
        var entity = new MedicalRecordTestBuilder().withPatientId(11L).build();
        medicalRecordRepository.save(entity);

        mockMvc.perform(get("/api/v1/medical-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @WithMockUserPrincipal(
            username = "admin",
            roles = {"ROLE_ADMIN"},
            permissions = {"MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ"}
    )
    @DisplayName("Should update a medical record")
    void shouldUpdateMedicalRecord() throws Exception {
        var entity = new MedicalRecordTestBuilder().withTitle("Old Title").build();
        var saved = medicalRecordRepository.save(entity);

        MedicalRecordDTO updateDto = new MedicalRecordDTO();
        updateDto.setPatientId(saved.getPatientId());
        updateDto.setDoctorId(saved.getDoctorId());
        updateDto.setRecordType(saved.getRecordType());
        updateDto.setRecordDate(saved.getRecordDate());
        updateDto.setTitle("New Title");

        mockMvc.perform(put("/api/v1/medical-records/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }


    @Test
    @WithMockUserPrincipal(
            username = "admin",
            roles = {"ROLE_ADMIN"},
            permissions = {"MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ"}
    )
    @DisplayName("Should finalize a medical record")
    void shouldFinalizeMedicalRecord() throws Exception {
        var entity = new MedicalRecordTestBuilder().withStatus(RecordStatus.DRAFT).build();
        var saved = medicalRecordRepository.save(entity);

        mockMvc.perform(patch("/api/v1/medical-records/{id}/finalize", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZED"));
    }

    @Test
    @WithMockUserPrincipal(
            username = "admin",
            roles = {"ROLE_ADMIN"},
            permissions = {"MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ"}
    )
    @DisplayName("Should sign a medical record")
    void shouldSignMedicalRecord() throws Exception {
        var entity = new MedicalRecordTestBuilder().withStatus(RecordStatus.FINALIZED).build();
        var saved = medicalRecordRepository.save(entity);

        mockMvc.perform(patch("/api/v1/medical-records/{id}/sign", saved.getId())
                        .param("signedBy", "Dr. Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SIGNED"))
                .andExpect(jsonPath("$.signedBy").value("Dr. Test"));
    }

    @Test
    @WithMockUserPrincipal(
            username = "admin",
            roles = {"ROLE_ADMIN"},
            permissions = {"MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ"}
    )
    @DisplayName("Should archive a medical record")
    void shouldArchiveMedicalRecord() throws Exception {
        var entity = new MedicalRecordTestBuilder().withStatus(RecordStatus.SIGNED).build();
        var saved = medicalRecordRepository.save(entity);

        mockMvc.perform(patch("/api/v1/medical-records/{id}/archive", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ARCHIVED"));
    }

    @Test
    @WithMockUserPrincipal(
            username = "admin",
            roles = {"ROLE_ADMIN"},
            permissions = {"MEDICAL_RECORD_CREATE", "MEDICAL_RECORD_READ"}
    )
    @DisplayName("Should delete a medical record")
    void shouldDeleteMedicalRecord() throws Exception {
        var entity = new MedicalRecordTestBuilder().build();
        var saved = medicalRecordRepository.save(entity);

        mockMvc.perform(delete("/api/v1/medical-records/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        assertThat(medicalRecordRepository.existsById(saved.getId())).isFalse();
    }
}