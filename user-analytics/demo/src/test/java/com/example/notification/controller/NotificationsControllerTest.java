package com.example.notification.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.notification.dto.NotificationsDTO;
import com.example.notification.model.NotificationPriority;
import com.example.notification.model.NotificationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {
        com.example.demo.UserAnalyticsJavaApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class NotificationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @BeforeEach
    void setup() {
        User mockUser = new User();
        mockUser.setId(1L);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserService userService() {
            User mockUser = new User();
            mockUser.setId(1L);
            UserService userService = Mockito.mock(UserService.class);
            Mockito.when(userService.getAuthenticatedUser()).thenReturn(java.util.Optional.of(mockUser));
            return userService;
        }
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/notifications returns 200")
    void getAllNotifications_returnsOk() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/notifications/unread returns 200")
    void getUnreadNotifications_returnsOk() throws Exception {
        mockMvc.perform(get("/api/notifications/unread"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/notifications/type/{type} returns 200")
    void getNotificationsByType_returnsOk() throws Exception {
        mockMvc.perform(get("/api/notifications/type/{type}", NotificationType.INFO))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/notifications/priority/{priority} returns 200")
    void getNotificationsByPriority_returnsOk() throws Exception {
        mockMvc.perform(get("/api/notifications/priority/{priority}", NotificationPriority.LOW))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/notifications/search returns 200")
    void searchNotifications_returnsOk() throws Exception {
        mockMvc.perform(get("/api/notifications/search").param("q", "test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/notifications/unread/count returns 200")
    void getUnreadCount_returnsOk() throws Exception {
        mockMvc.perform(get("/api/notifications/unread/count"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/notifications creates notification")
    void createNotification_returnsCreated() throws Exception {
        NotificationsDTO dto = NotificationsDTO.builder()
                .title("Test Notification")
                .message("Test message")
                .timestamp(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .type(NotificationType.INFO)
                .priority(NotificationPriority.LOW)
                .build();

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/notifications/{id}/read marks as read")
    void markAsRead_returnsOk() throws Exception {
        mockMvc.perform(post("/api/notifications/{id}/read", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/notifications/read-all marks all as read")
    void markAllAsRead_returnsOk() throws Exception {
        mockMvc.perform(post("/api/notifications/read-all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/notifications/{id} deletes notification")
    void deleteNotification_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/notifications/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/notifications/read deletes all read notifications")
    void deleteAllReadNotifications_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/notifications/read"))
                .andExpect(status().isNoContent());
    }
}
