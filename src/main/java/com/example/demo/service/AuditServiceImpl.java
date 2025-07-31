package com.example.demo.service;

import com.example.demo.dto.AuditDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.*;
import com.example.demo.repository.AuditRepository;
import com.example.demo.repository.ProfileAuditRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.impl.AuditService;
import com.example.demo.util.DateTimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AuditServiceImpl implements AuditService {

    private final ProfileAuditRepository profileAuditRepository;
    private final AuditRepository auditRepository;
    private final UserService userService;
    private final ProfileRepository profileRepository;


    public Audit saveAudit(AuditDTO auditDTO) {

        Audit audit = Audit.builder()
                .user(auditDTO.getUser())
                .action(auditDTO.getAction())
                .target(auditDTO.getTarget())
                .timestamp(DateTimeConverter.convertTimestampStringToTimestamp(auditDTO.getTimestamp()))
                .build();

        return auditRepository.saveAndFlush(audit);
    }

    public ResponseDTO createAudit(AuditDTO auditDTO) {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        // Check if the audit already exists
        if (auditRepository.findByUser(auditDTO.getUser()).isEmpty()) {
            return ResponseDTO.builder()
                    .status("error")
                    .message("Audit with this title already exists")
                    .build();
        }

        // Create and save the audit
        Long auditId = auditRepository.saveAndFlush(Audit.builder()
                .user(user.getUsername())
                .action(auditDTO.getAction())
                .target(auditDTO.getTarget())
                .timestamp(DateTimeConverter.convertTimestampStringToTimestamp(auditDTO.getTimestamp()))
                .build()).getId();

        // Save the profile audit
        saveProfileAudit(auditId);

        return ResponseDTO.builder()
                .status("success")
                .message("Audit created successfully")
                .data(auditId)
                .build();
    }

    private Boolean saveProfileAudit(Long auditId) {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        Profile profile = profileRepository.findProfilesByUserId(user.getUserId())
                .stream()
                .reduce((first, second) -> second)
                .orElseGet(() -> profileRepository.saveAndFlush(new Profile()));

        ProfileAudit profileAudit = ProfileAudit.builder()
                .profileId(profile.getId())
                .auditId(auditId)
                .build();

        profileAuditRepository.saveAndFlush(profileAudit);
        return true; // Assuming the save operation is successful
    }


    public List<AuditDTO> getAllAudits() {
        return auditRepository.findAll()
                .stream()
                .map(audit -> AuditDTO.builder()
                        .user(audit.getUser())
                        .action(audit.getAction())
                        .target(audit.getTarget())
                        .timestamp(DateTimeConverter.convertTimestampToString(audit.getTimestamp()))
                        .build())
                .toList();
    }

    @Override
    public List<AuditDTO> getAuditsByUser(String username) {
        return List.of();
    }
}
