/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.activity.controller;

import com.example.activity.dto.ActivityDTO;
import com.example.activity.model.ActivityType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {
        com.example.demo.UserAnalyticsJavaApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ActivityDTO activityDTO;

    @BeforeEach
    void setUp() {
        activityDTO = ActivityDTO.builder()
                .userId(1L)
                .username("testuser")
                .type(ActivityType.LOGIN)
                .description("Integration test activity")
                .ipAddress("127.0.0.1")
                .deviceType("Desktop")
                .build();
    }


    @Test
    @WithMockUser
    void createAndRetrieveActivity_fullCycle() throws Exception {
        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activityDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));

        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
