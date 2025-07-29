package com.example.demo.controller;

import com.example.demo.dto.NotificationsDTO;
import com.example.demo.service.impl.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> getNotifications() {
        log.info("Fetching notifications");
        // Mock data for demonstration purposes
        List<NotificationsDTO> notificationsDTOList = new ArrayList<>();
        notificationsDTOList.add(new NotificationsDTO.Builder()
                .title("New Feature Release")
                .timestamp("2023-10-01T10:00:00Z")
                .message("We have released a new feature that enhances your experience.")
                .build());
        notificationsDTOList.add(new NotificationsDTO.Builder()
                .title("Scheduled Maintenance")
                .timestamp("2023-10-02T12:00:00Z")
                .message("Our system will undergo maintenance on 2023-10-03 from 2 AM to 4 AM.")
                .build());
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

}
