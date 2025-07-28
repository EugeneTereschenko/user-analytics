package com.example.demo.service.impl;

import com.example.demo.dto.AnnouncementDTO;

import java.util.List;

public interface AnnouncementService {
    List<AnnouncementDTO> getAllAnnouncements();
    void saveAnnouncement(AnnouncementDTO announcementDTO);
}
