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
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

}
