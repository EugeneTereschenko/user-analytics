/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.announcement.controller;

import com.example.announcement.dto.AnnouncementDTO;
import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;
import com.example.announcement.service.impl.AnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "http://localhost:4200")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        log.info("GET /api/announcements");
        List<AnnouncementDTO> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> getAnnouncementById(@PathVariable Long id) {
        log.info("GET /api/announcements/{}", id);
        AnnouncementDTO announcement = announcementService.getAnnouncementById(id);
        return ResponseEntity.ok(announcement);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementsByPriority(
            @PathVariable AnnouncementPriority priority) {
        log.info("GET /api/announcements/priority/{}", priority);
        List<AnnouncementDTO> announcements = announcementService.getAnnouncementsByPriority(priority);
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementsByCategory(
            @PathVariable AnnouncementCategory category) {
        log.info("GET /api/announcements/category/{}", category);
        List<AnnouncementDTO> announcements = announcementService.getAnnouncementsByCategory(category);
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AnnouncementDTO>> getActiveAnnouncements() {
        log.info("GET /api/announcements/active");
        List<AnnouncementDTO> announcements = announcementService.getActiveAnnouncements();
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<AnnouncementDTO>> getUnreadAnnouncements() {
        log.info("GET /api/announcements/unread");
        List<AnnouncementDTO> announcements = announcementService.getUnreadAnnouncements();
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnnouncementDTO>> searchAnnouncements(@RequestParam String q) {
        log.info("GET /api/announcements/search?q={}", q);
        List<AnnouncementDTO> announcements = announcementService.searchAnnouncements(q);
        return ResponseEntity.ok(announcements);
    }

    @PostMapping
    public ResponseEntity<AnnouncementDTO> createAnnouncement(
            @RequestBody AnnouncementDTO announcementDTO) {
        log.info("POST /api/announcements");
        AnnouncementDTO created = announcementService.createAnnouncement(announcementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody AnnouncementDTO announcementDTO) {
        log.info("PUT /api/announcements/{}", id);
        AnnouncementDTO updated = announcementService.updateAnnouncement(id, announcementDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        log.info("DELETE /api/announcements/{}", id);
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("POST /api/announcements/{}/read", id);
        announcementService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        log.info("POST /api/announcements/read-all");
        announcementService.markAllAsRead();
        return ResponseEntity.ok().build();
    }
}
