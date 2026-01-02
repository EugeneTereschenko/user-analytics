/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.announcement.service.impl;

import com.example.announcement.dto.AnnouncementDTO;
import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementDTO> getAllAnnouncements();

    AnnouncementDTO getAnnouncementById(Long id);

    List<AnnouncementDTO> getAnnouncementsByPriority(AnnouncementPriority priority);

    List<AnnouncementDTO> getAnnouncementsByCategory(AnnouncementCategory category);

    List<AnnouncementDTO> getActiveAnnouncements();

    List<AnnouncementDTO> getUnreadAnnouncements();

    List<AnnouncementDTO> searchAnnouncements(String query);

    @Transactional
    AnnouncementDTO createAnnouncement(AnnouncementDTO announcementDTO);

    @Transactional
    AnnouncementDTO updateAnnouncement(Long id, AnnouncementDTO announcementDTO);

    @Transactional
    void deleteAnnouncement(Long id);

    @Transactional
    void markAsRead(Long announcementId);

    @Transactional
    void markAllAsRead();
}
