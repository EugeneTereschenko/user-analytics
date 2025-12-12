/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.announcement.service;

import com.example.announcement.dto.AnnouncementDTO;
import com.example.announcement.model.Announcement;
import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;
import com.example.announcement.model.UserAnnouncementRead;
import com.example.announcement.service.impl.AnnouncementService;
import com.example.demo.model.User;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.UserAnnouncementReadRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserAnnouncementReadRepository readRepository;
    private final UserService userService;

    @Override
    public List<AnnouncementDTO> getAllAnnouncements() {
        log.info("Fetching all announcements");
        Long currentUserId = getCurrentUserId();

        return announcementRepository.findAllByOrderByDateDesc().stream()
                .map(announcement -> convertToDTO(announcement, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    public AnnouncementDTO getAnnouncementById(Long id) {
        log.info("Fetching announcement with id: {}", id);
        Long currentUserId = getCurrentUserId();

        return announcementRepository.findById(id)
                .map(announcement -> convertToDTO(announcement, currentUserId))
                .orElseThrow(() -> new RuntimeException("Announcement not found"));
    }

    @Override
    public List<AnnouncementDTO> getAnnouncementsByPriority(AnnouncementPriority priority) {
        log.info("Fetching announcements with priority: {}", priority);
        Long currentUserId = getCurrentUserId();

        return announcementRepository.findByPriorityOrderByDateDesc(priority).stream()
                .map(announcement -> convertToDTO(announcement, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnouncementDTO> getAnnouncementsByCategory(AnnouncementCategory category) {
        log.info("Fetching announcements with category: {}", category);
        Long currentUserId = getCurrentUserId();

        return announcementRepository.findByCategory(category).stream()
                .map(announcement -> convertToDTO(announcement, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnouncementDTO> getActiveAnnouncements() {
        log.info("Fetching active announcements");
        Long currentUserId = getCurrentUserId();

        return announcementRepository.findActiveNonExpired(LocalDateTime.now()).stream()
                .map(announcement -> convertToDTO(announcement, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnouncementDTO> getUnreadAnnouncements() {
        log.info("Fetching unread announcements");
        Long currentUserId = getCurrentUserId();

        return announcementRepository.findAllByOrderByDateDesc().stream()
                .filter(announcement -> !isRead(currentUserId, announcement.getId()))
                .map(announcement -> convertToDTO(announcement, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnouncementDTO> searchAnnouncements(String query) {
        log.info("Searching announcements with query: {}", query);
        Long currentUserId = getCurrentUserId();

        return announcementRepository.searchAnnouncements(query).stream()
                .map(announcement -> convertToDTO(announcement, currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AnnouncementDTO createAnnouncement(AnnouncementDTO announcementDTO) {
        log.info("Creating announcement: {}", announcementDTO.getTitle());

        Announcement announcement = Announcement.builder()
                .title(announcementDTO.getTitle())
                .body(announcementDTO.getBody())
                .date(parseDate(announcementDTO.getDate()))
                .author(announcementDTO.getAuthor())
                .priority(announcementDTO.getPriority())
                .category(announcementDTO.getCategory())
                .isActive(true)
                .build();

        Announcement saved = announcementRepository.save(announcement);
        return convertToDTO(saved, getCurrentUserId());
    }

    @Transactional
    @Override
    public AnnouncementDTO updateAnnouncement(Long id, AnnouncementDTO announcementDTO) {
        log.info("Updating announcement: {}", id);

        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        announcement.setTitle(announcementDTO.getTitle());
        announcement.setBody(announcementDTO.getBody());
        announcement.setPriority(announcementDTO.getPriority());
        announcement.setCategory(announcementDTO.getCategory());

        Announcement updated = announcementRepository.save(announcement);
        return convertToDTO(updated, getCurrentUserId());
    }

    @Transactional
    @Override
    public void deleteAnnouncement(Long id) {
        log.info("Deleting announcement: {}", id);
        announcementRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void markAsRead(Long announcementId) {
        Long userId = getCurrentUserId();
        log.info("Marking announcement {} as read for user {}", announcementId, userId);

        if (!readRepository.existsByUserIdAndAnnouncementId(userId, announcementId)) {
            UserAnnouncementRead read = new UserAnnouncementRead();
            read.setUserId(userId);
            read.setAnnouncementId(announcementId);
            readRepository.save(read);
        }
    }

    @Transactional
    @Override
    public void markAllAsRead() {
        Long userId = getCurrentUserId();
        log.info("Marking all announcements as read for user {}", userId);

        List<Announcement> announcements = announcementRepository.findAll();
        for (Announcement announcement : announcements) {
            if (!readRepository.existsByUserIdAndAnnouncementId(userId, announcement.getId())) {
                UserAnnouncementRead read = new UserAnnouncementRead();
                read.setUserId(userId);
                read.setAnnouncementId(announcement.getId());
                readRepository.save(read);
            }
        }
    }

    private AnnouncementDTO convertToDTO(Announcement announcement, Long userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        return new AnnouncementDTO.Builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .body(announcement.getBody())
                .date(sdf.format(announcement.getDate()))
                .author(announcement.getAuthor())
                .priority(announcement.getPriority())
                .category(announcement.getCategory())
                .isRead(isRead(userId, announcement.getId()))
                .expiryDate(announcement.getExpiryDate() != null ?
                        announcement.getExpiryDate().toString() : null)
                .build();
    }

    private boolean isRead(Long userId, Long announcementId) {
        return readRepository.existsByUserIdAndAnnouncementId(userId, announcementId);
    }

    private Long getCurrentUserId() {
        return userService.getAuthenticatedUser()
                .map(User::getId)
                .orElse(0L);
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return sdf.parse(dateString);
        } catch (Exception e) {
            return new Date();
        }
    }
}
