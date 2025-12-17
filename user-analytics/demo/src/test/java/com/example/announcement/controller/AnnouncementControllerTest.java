/*
 * © 2024 Yevhen Tereshchenko
 * All rights reserved.
 */

package com.example.announcement.controller;

import com.example.announcement.dto.AnnouncementDTO;
import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;
import com.example.announcement.service.impl.AnnouncementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {
        com.example.demo.UserAnalyticsJavaApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Announcement Controller Tests")
class AnnouncementControllerTest {




    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AnnouncementService announcementService;

    @Autowired
    private ObjectMapper objectMapper;

    private AnnouncementDTO announcementDTO;
    private List<AnnouncementDTO> announcementList;

    @TestConfiguration
    static class MockConfig {
        @Bean
        AnnouncementService announcementService() {
            return org.mockito.Mockito.mock(AnnouncementService.class);
        }
    }

    @BeforeEach
    void setUp() {
        announcementDTO = AnnouncementDTO.builder()
                .id(1L)
                .title("Test Announcement")
                .body("This is a test announcement body")
                .date("2024-12-16T10:00:00")
                .author("Test Author")
                .priority(AnnouncementPriority.MEDIUM)
                .category(AnnouncementCategory.GENERAL)
                .isRead(false)
                .build();

        AnnouncementDTO announcement2 = AnnouncementDTO.builder()
                .id(2L)
                .title("Urgent Announcement")
                .body("This is urgent")
                .date("2024-12-16T11:00:00")
                .author("Admin")
                .priority(AnnouncementPriority.URGENT)
                .category(AnnouncementCategory.SECURITY)
                .isRead(false)
                .build();

        announcementList = Arrays.asList(announcementDTO, announcement2);
    }
    @Test
    @DisplayName("GET /api/announcements - Should return all announcements")
    void getAllAnnouncements_shouldReturnAllAnnouncements() throws Exception {
        // Given
        when(announcementService.getAllAnnouncements()).thenReturn(announcementList);

        // When & Then
        mockMvc.perform(get("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Announcement"))
                .andExpect(jsonPath("$[1].priority").value("URGENT"));

        verify(announcementService, times(1)).getAllAnnouncements();
    }
    @Test
    @DisplayName("GET /api/announcements - Should return empty list when no announcements")
    void getAllAnnouncements_emptyList_shouldReturnEmptyArray() throws Exception {
        // Given
        when(announcementService.getAllAnnouncements()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/announcements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/announcements/{id} - Should return announcement by ID")
    void getAnnouncementById_validId_shouldReturnAnnouncement() throws Exception {
        // Given
        when(announcementService.getAnnouncementById(1L)).thenReturn(announcementDTO);

        // When & Then
        mockMvc.perform(get("/api/announcements/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Announcement"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.category").value("GENERAL"));

        verify(announcementService, times(1)).getAnnouncementById(1L);
    }

    @Test
    @DisplayName("GET /api/announcements/{id} - Should return 500 when announcement not found")
    void getAnnouncementById_invalidId_shouldReturnNotFound() throws Exception {
        // Given
        when(announcementService.getAnnouncementById(999L))
                .thenThrow(new RuntimeException("Announcement not found"));

        // When & Then
        mockMvc.perform(get("/api/announcements/{id}", 999L))
                .andExpect(status().isInternalServerError());

        verify(announcementService, times(1)).getAnnouncementById(999L);
    }

    @Test
    @DisplayName("GET /api/announcements/priority/{priority} - Should return announcements by priority")
    void getAnnouncementsByPriority_shouldReturnFilteredAnnouncements() throws Exception {
        // Given
        List<AnnouncementDTO> urgentAnnouncements = Collections.singletonList(
                announcementList.get(1)
        );
        when(announcementService.getAnnouncementsByPriority(AnnouncementPriority.URGENT))
                .thenReturn(urgentAnnouncements);

        // When & Then
        mockMvc.perform(get("/api/announcements/priority/{priority}", "URGENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].priority").value("URGENT"))
                .andExpect(jsonPath("$[0].title").value("Urgent Announcement"));

        verify(announcementService, times(1))
                .getAnnouncementsByPriority(AnnouncementPriority.URGENT);
    }

    @Test
    @DisplayName("GET /api/announcements/category/{category} - Should return announcements by category")
    void getAnnouncementsByCategory_shouldReturnFilteredAnnouncements() throws Exception {
        // Given
        List<AnnouncementDTO> securityAnnouncements = Collections.singletonList(
                announcementList.get(1)
        );
        when(announcementService.getAnnouncementsByCategory(AnnouncementCategory.SECURITY))
                .thenReturn(securityAnnouncements);

        // When & Then
        mockMvc.perform(get("/api/announcements/category/{category}", "SECURITY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].category").value("SECURITY"));

        verify(announcementService, times(1))
                .getAnnouncementsByCategory(AnnouncementCategory.SECURITY);
    }

    @Test
    @DisplayName("GET /api/announcements/active - Should return active announcements")
    void getActiveAnnouncements_shouldReturnActiveOnly() throws Exception {
        // Given
        when(announcementService.getActiveAnnouncements()).thenReturn(announcementList);

        // When & Then
        mockMvc.perform(get("/api/announcements/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(announcementService, times(1)).getActiveAnnouncements();
    }

    @Test
    @DisplayName("GET /api/announcements/unread - Should return unread announcements")
    void getUnreadAnnouncements_shouldReturnUnreadOnly() throws Exception {
        // Given
        when(announcementService.getUnreadAnnouncements()).thenReturn(announcementList);

        // When & Then
        mockMvc.perform(get("/api/announcements/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].isRead").value(false))
                .andExpect(jsonPath("$[1].isRead").value(false));

        verify(announcementService, times(1)).getUnreadAnnouncements();
    }

    @Test
    @DisplayName("GET /api/announcements/search - Should return announcements matching query")
    void searchAnnouncements_shouldReturnMatchingResults() throws Exception {
        // Given
        String query = "test";
        when(announcementService.searchAnnouncements(query))
                .thenReturn(Collections.singletonList(announcementDTO));

        // When & Then
        mockMvc.perform(get("/api/announcements/search")
                        .param("q", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", containsString("Test")));

        verify(announcementService, times(1)).searchAnnouncements(query);
    }

    @Test
    @DisplayName("POST /api/announcements - Should create new announcement")
    void createAnnouncement_validData_shouldReturnCreated() throws Exception {
        // Given
        when(announcementService.createAnnouncement(any(AnnouncementDTO.class)))
                .thenReturn(announcementDTO);

        // When & Then
        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(announcementDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Announcement"));

        verify(announcementService, times(1)).createAnnouncement(any(AnnouncementDTO.class));
    }

    @Test
    @DisplayName("POST /api/announcements - Should handle invalid data")
    void createAnnouncement_invalidData_shouldReturnBadRequest() throws Exception {
        // Given
        AnnouncementDTO invalidAnnouncement = AnnouncementDTO.builder().build();
        when(announcementService.createAnnouncement(any(AnnouncementDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid announcement data"));

        // When & Then
        mockMvc.perform(post("/api/announcements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAnnouncement)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("PUT /api/announcements/{id} - Should update announcement")
    void updateAnnouncement_validData_shouldReturnUpdated() throws Exception {
        // Given
        AnnouncementDTO updatedAnnouncement = AnnouncementDTO.builder()
                .id(1L)
                .title("Updated Title")
                .body("Updated body")
                .date("2024-12-16T10:00:00")
                .author("Test Author")
                .priority(AnnouncementPriority.HIGH)
                .category(AnnouncementCategory.MAINTENANCE)
                .build();

        when(announcementService.updateAnnouncement(eq(1L), any(AnnouncementDTO.class)))
                .thenReturn(updatedAnnouncement);

        // When & Then
        mockMvc.perform(put("/api/announcements/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAnnouncement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(announcementService, times(1))
                .updateAnnouncement(eq(1L), any(AnnouncementDTO.class));
    }

    @Test
    @DisplayName("PUT /api/announcements/{id} - Should handle non-existent announcement")
    void updateAnnouncement_nonExistentId_shouldReturnNotFound() throws Exception {
        // Given
        when(announcementService.updateAnnouncement(eq(999L), any(AnnouncementDTO.class)))
                .thenThrow(new RuntimeException("Announcement not found"));

        // When & Then
        mockMvc.perform(put("/api/announcements/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(announcementDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("DELETE /api/announcements/{id} - Should delete announcement")
    void deleteAnnouncement_validId_shouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(announcementService).deleteAnnouncement(1L);

        // When & Then
        mockMvc.perform(delete("/api/announcements/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(announcementService, times(1)).deleteAnnouncement(1L);
    }

    @Test
    @DisplayName("DELETE /api/announcements/{id} - Should handle deletion error")
    void deleteAnnouncement_error_shouldReturnInternalServerError() throws Exception {
        // Given
        doThrow(new RuntimeException("Database error"))
                .when(announcementService).deleteAnnouncement(999L);

        // When & Then
        mockMvc.perform(delete("/api/announcements/{id}", 999L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("POST /api/announcements/{id}/read - Should mark announcement as read")
    void markAsRead_validId_shouldReturnOk() throws Exception {
        // Given
        doNothing().when(announcementService).markAsRead(1L);

        // When & Then
        mockMvc.perform(post("/api/announcements/{id}/read", 1L))
                .andExpect(status().isOk());

        verify(announcementService, times(1)).markAsRead(1L);
    }

    @Test
    @DisplayName("POST /api/announcements/read-all - Should mark all announcements as read")
    void markAllAsRead_shouldReturnOk() throws Exception {
        // Given
        doNothing().when(announcementService).markAllAsRead();

        // When & Then
        mockMvc.perform(post("/api/announcements/read-all"))
                .andExpect(status().isOk());

        verify(announcementService, times(1)).markAllAsRead();
    }
    @Test
    @DisplayName("GET /api/announcements - Should handle service exception")
    void getAllAnnouncements_serviceException_shouldReturnError() throws Exception {
        // Given
        when(announcementService.getAllAnnouncements())
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(get("/api/announcements"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @DisplayName("GET /api/announcements/search - Should handle empty query")
    void searchAnnouncements_emptyQuery_shouldReturnEmptyList() throws Exception {
        // Given
        when(announcementService.searchAnnouncements(""))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/announcements/search").param("q", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    @DisplayName("POST /api/announcements - Should handle all priority types")
    void createAnnouncement_allPriorities_shouldSucceed() throws Exception {
        // Test each priority level
        for (AnnouncementPriority priority : AnnouncementPriority.values()) {
            AnnouncementDTO dto = AnnouncementDTO.builder()
                    .title("Test " + priority)
                    .body("Test body")
                    .date("2024-12-16T10:00:00")
                    .priority(priority)
                    .category(AnnouncementCategory.GENERAL)
                    .build();

            when(announcementService.createAnnouncement(any(AnnouncementDTO.class)))
                    .thenReturn(dto);

            mockMvc.perform(post("/api/announcements")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.priority").value(priority.toString()));
        }
    }

    @Test
    @DisplayName("POST /api/announcements - Should handle all category types")
    void createAnnouncement_allCategories_shouldSucceed() throws Exception {
        // Test each category
        for (AnnouncementCategory category : AnnouncementCategory.values()) {
            AnnouncementDTO dto = AnnouncementDTO.builder()
                    .title("Test " + category)
                    .body("Test body")
                    .date("2024-12-16T10:00:00")
                    .priority(AnnouncementPriority.MEDIUM)
                    .category(category)
                    .build();

            when(announcementService.createAnnouncement(any(AnnouncementDTO.class)))
                    .thenReturn(dto);

            mockMvc.perform(post("/api/announcements")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.category").value(category.toString()));
        }
    }

}