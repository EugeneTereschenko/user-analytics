package com.example.demo.service;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.SupportDTO;
import com.example.demo.model.*;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.ProfileSupportRepository;
import com.example.demo.repository.SupportRepository;
import com.example.demo.service.impl.SupportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;
    private final ProfileSupportRepository profileSupportRepository;
    private final ProfileRepository profileRepository;
    private final UserService userService;

    public ResponseDTO createSupport(SupportDTO supportDTO) {
        if (supportRepository.findBySubject(supportDTO.getSubject()).stream().findAny().isPresent()) {
            // If a support request with the same subject already exists, return an error response
            return ResponseDTO.builder()
                    .status("error")
                    .message("Support request with this subject already exists")
                    .build();

        }

        Support existingSupport = supportRepository.save(new Support.Builder()
                .subject(supportDTO.getSubject())
                .message(supportDTO.getMessage())
                .successMessage("Support created successfully")
                .build());

        saveProfileSupport(existingSupport.getId());
        return ResponseDTO.builder()
                .message("Support request created successfully")
                .status("success")
                .build();
    }

    private Boolean saveProfileSupport(Long supportId) {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        Profile profile = profileRepository.findProfilesByUserId(user.getUserId())
                .stream()
                .reduce((first, second) -> second)
                .orElseGet(() -> profileRepository.saveAndFlush(new Profile()));

        ProfileSupport profileSupport = ProfileSupport.builder()
                .profileId(profile.getId())
                .supportId(supportId)
                .build();

        profileSupportRepository.saveAndFlush(profileSupport);
        return true; // Assuming the save operation is successful
    }
}
