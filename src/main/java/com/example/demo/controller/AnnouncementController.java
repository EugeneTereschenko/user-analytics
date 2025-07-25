package com.example.demo.controller;

import com.example.demo.dto.AnnouncementDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @GetMapping
    public ResponseEntity<?> getAnnouncements() {
        log.info("Fetching announcements");
        // Here you would typically fetch announcements from a service or database

        List<AnnouncementDTO> announcements = List.of(
                new AnnouncementDTO.Builder()
                        .title("Announcement 1")
                        .body("This is the content of announcement 2.")
                        .date("2023-10-02")
                        .build(),
                new AnnouncementDTO.Builder()
                        .title("Announcement 2")
                        .body("This is the content of announcement 3.")
                        .date("2023-10-03")
                        .build(),
                new AnnouncementDTO.Builder()
                        .title("Announcement 3")
                        .body("This is the content of announcement 1.")
                        .date("2023-10-01")
                        .build());

        return ResponseEntity.ok(announcements);
    }
}
