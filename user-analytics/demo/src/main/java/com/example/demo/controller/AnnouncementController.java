package com.example.demo.controller;

import com.example.demo.dto.AnnouncementDTO;
import com.example.demo.service.impl.AnnouncementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public ResponseEntity<?> getAnnouncements() {
        List<AnnouncementDTO> announcements = announcementService.getAllAnnouncements();
        log.info("Fetching announcements {}", announcements);
        return ResponseEntity.ok(announcements);
    }
}
