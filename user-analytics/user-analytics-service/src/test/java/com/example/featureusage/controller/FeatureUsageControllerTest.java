/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
        com.example.demo.UserAnalyticsJavaApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FeatureUsageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getFeatureUsage_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/usage/feature")
                        .param("period", "weekly")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}