package com.healthcare.doctorservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.doctorservice.DoctorServiceApplication;
import com.healthcare.doctorservice.dto.DoctorDTO;
import com.healthcare.doctorservice.entity.DoctorStatus;
import com.healthcare.doctorservice.testutil.DoctorDTOTestBuilder;
import com.healthcare.doctorservice.testutil.DoctorTestBuilder;
import com.healthcare.doctorservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {DoctorServiceApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createDoctor_shouldReturnCreatedDoctor() throws Exception {
        DoctorDTO doctorDTO = DoctorDTOTestBuilder.defaultDoctor().build();

        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(doctorDTO.getEmail()));
    }

    @Test
    void getDoctorById_shouldReturnDoctor() throws Exception {
        DoctorDTO doctorDTO = DoctorDTOTestBuilder.defaultDoctor().build();

        String response = mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        DoctorDTO createdDoctor = objectMapper.readValue(response, DoctorDTO.class);

        mockMvc.perform(get("/api/v1/doctors/{id}", createdDoctor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdDoctor.getId()))
                .andExpect(jsonPath("$.email").value(doctorDTO.getEmail()));
    }
}
