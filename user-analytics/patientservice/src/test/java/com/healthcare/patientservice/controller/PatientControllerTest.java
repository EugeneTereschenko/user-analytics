package com.healthcare.patientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patientservice.PatientServiceApplication;
import com.healthcare.patientservice.dto.PatientDTO;
import com.healthcare.patientservice.entity.Gender;
import com.healthcare.patientservice.entity.PatientStatus;
import com.healthcare.patientservice.testutil.TestcontainersConfiguration;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {PatientServiceApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Should create a patient successfully")
    void createPatient_Success() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setEmail("john.doe@example.com");
        patientDTO.setFirstName("John");
        patientDTO.setLastName("Doe");
        patientDTO.setStatus(PatientStatus.ACTIVE);
        patientDTO.setPhoneNumber("1234567890");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender(Gender.MALE);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    @DisplayName("Should get patient by id")
    void getPatientById_Success() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setEmail("jane.doe@example.com");
        patientDTO.setFirstName("Jane");
        patientDTO.setLastName("Doe");
        patientDTO.setStatus(PatientStatus.ACTIVE);
        patientDTO.setPhoneNumber("1234567891");
        patientDTO.setDateOfBirth(LocalDate.of(1991, 2, 2));
        patientDTO.setGender(Gender.FEMALE);

        String response = mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PatientDTO created = objectMapper.readValue(response, PatientDTO.class);

        mockMvc.perform(get("/api/v1/patients/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("jane.doe@example.com")));
    }

    @Test
    @DisplayName("Should delete patient by id")
    void deletePatient_Success() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setEmail("delete.me@example.com");
        patientDTO.setFirstName("Delete");
        patientDTO.setLastName("Me");
        patientDTO.setStatus(PatientStatus.ACTIVE);
        patientDTO.setPhoneNumber("1234567892");
        patientDTO.setDateOfBirth(LocalDate.of(1992, 3, 3));
        patientDTO.setGender(Gender.OTHER);

        String response = mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        PatientDTO created = objectMapper.readValue(response, PatientDTO.class);

        mockMvc.perform(delete("/api/v1/patients/{id}", created.getId()))
                .andExpect(status().isNoContent());
    }

}
