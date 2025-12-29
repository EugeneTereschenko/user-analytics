/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.appointmentservice.AppointmentServiceApplication;
import com.healthcare.appointmentservice.dto.AppointmentDTO;
import com.healthcare.appointmentservice.testutil.AppointmentTestBuilder;
import com.healthcare.appointmentservice.testutil.TestcontainersConfiguration;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {AppointmentServiceApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create appointment successfully")
    void createAppointment_success() throws Exception {
        AppointmentDTO appointmentDTO = AppointmentTestBuilder.defaultAppointmentDTO();

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorId").value(appointmentDTO.getDoctorId()))
                .andExpect(jsonPath("$.patientId").value(appointmentDTO.getPatientId()))
                .andExpect(jsonPath("$.status").value(appointmentDTO.getStatus().toString()));
    }
}