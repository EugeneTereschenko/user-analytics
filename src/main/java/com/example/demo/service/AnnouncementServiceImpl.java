package com.example.demo.service;

import com.example.demo.dto.AnnouncementDTO;
import com.example.demo.model.Announcement;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.service.impl.AnnouncementService;
import com.example.demo.util.DateTimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;


    public List<AnnouncementDTO> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .map(announcement -> new AnnouncementDTO.Builder()
                        .title(announcement.getTitle())
                        .body(announcement.getBody())
                        .date(announcement.getDate().toString())
                        .build())
                .collect(Collectors.toList());
    }

    public void saveAnnouncement(AnnouncementDTO announcementDTO) {
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setBody(announcementDTO.getBody());
        announcement.setDate(DateTimeConverter.convertDateStringToDate(announcementDTO.getDate()));
        announcementRepository.save(announcement);
    }
}
