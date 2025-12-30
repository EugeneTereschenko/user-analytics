package com.healthcare.doctorservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.doctorservice.DoctorServiceApplication;
import com.healthcare.doctorservice.dto.StaffDTO;
import com.healthcare.doctorservice.testutil.StaffDTOTestBuilder;
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
class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createStaff_shouldReturnCreatedStaff() throws Exception {
        StaffDTO staffDTO = StaffDTOTestBuilder.defaultStaff().build();

        mockMvc.perform(post("/api/v1/staff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staffDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(staffDTO.getEmail()));
    }

    @Test
    void getStaffById_shouldReturnStaff() throws Exception {
        StaffDTO staffDTO = StaffDTOTestBuilder.defaultStaff().build();

        String response = mockMvc.perform(post("/api/v1/staff")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staffDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        StaffDTO createdStaff = objectMapper.readValue(response, StaffDTO.class);

        mockMvc.perform(get("/api/v1/staff/{id}", createdStaff.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdStaff.getId()))
                .andExpect(jsonPath("$.email").value(staffDTO.getEmail()));
    }
}
