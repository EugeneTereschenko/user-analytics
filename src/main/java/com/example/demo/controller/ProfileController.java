package com.example.demo.controller;

import com.example.demo.dto.CertificateDTO;
import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.EducationDTO;
import com.example.demo.dto.DetailsDTO;
import com.example.demo.dto.ExperienceDTO;
import com.example.demo.dto.SkillsDTO;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;


    @PostMapping("profile/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String result = profileService.uploadImageToDatabase(file);
        if (!result.startsWith("Error")) {
            log.debug("Image upload successful: " + result);
            return ResponseEntity.ok(result);
        }
        log.error("Image upload failed: " + result);
        return ResponseEntity.status(500).body(result);
    }

    @GetMapping("profile/getImage")
    public ResponseEntity<byte[]> getImage() {
        byte[] imageDataResult = profileService.getImageForUser();
        if (imageDataResult != null) {
            log.debug("Image retrieval successful");
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg") // Adjust based on your image type
                    .body(imageDataResult);
        }
        log.error("Image retrieval failed");
        return ResponseEntity.status(500).body(null);
    }

    @GetMapping("profile/information")
    public ResponseEntity<?> getProfileInformation() {
        try {
            return ResponseEntity.ok(profileService.getProfileInformation());
        } catch (Exception e) {
            log.error("Error retrieving profile information: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving profile information: " + e.getMessage());
        }
    }

    @GetMapping("profile/certificate-dates")
    public ResponseEntity<?> getCertificateDates() {
        try {
            return ResponseEntity.ok(profileService.getCertificates());
        } catch (Exception e) {
            log.error("Error retrieving certificate dates: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving certificate dates: " + e.getMessage());
        }
    }

    @PostMapping("profile/update-certificate-dates")
    public ResponseEntity<?> updateCertificateDates(@RequestBody CertificateDTO certificateDTO) {
       try {
            log.info("Updating certificate dates: {}", certificateDTO);
            return ResponseEntity.ok(profileService.updateCertificateDates(certificateDTO));
        } catch (Exception e) {
            log.error("Error updating certificate dates: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error updating certificate dates: " + e.getMessage());
        }
    }

    @GetMapping("profile/get-profile")
    public ResponseEntity<?> getProfile() {
        try {
            return ResponseEntity.ok(profileService.getProfile());
        } catch (Exception e) {
            log.error("Error retrieving profile: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving profile: " + e.getMessage());
        }
    }

    @PostMapping("profile/update")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDTO profileDTO) {
        try {
            log.info("Updating profile: {}", profileDTO);
            return ResponseEntity.ok(profileService.updateProfile(profileDTO));
        } catch (Exception e) {
            log.error("Error updating profile: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error updating profile: " + e.getMessage());
        }
    }

    @GetMapping("profile/education")
    public ResponseEntity<?> getEducation() {
        try {
            return ResponseEntity.ok(profileService.getEducation());
        } catch (Exception e) {
            log.error("Error retrieving education: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving education: " + e.getMessage());
        }
    }

    @PostMapping("profile/update-education")
    public ResponseEntity<?> updateEducation(@RequestBody EducationDTO educationDTO) {
        try {
            log.info("Updating education: {}", educationDTO);
            return ResponseEntity.ok(profileService.updateEducation(educationDTO));
        } catch (Exception e) {
            log.error("Error updating education: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error updating education: " + e.getMessage());
        }
    }

    @GetMapping("profile/details")
    public ResponseEntity<?> getDetails() {
        try {
            return ResponseEntity.ok(profileService.getDetails());
        } catch (Exception e) {
            log.error("Error retrieving details: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving details: " + e.getMessage());
        }
    }


    @PostMapping("profile/update-details")
    public ResponseEntity<?> updateDetails(@RequestBody DetailsDTO detailsDTO) {
        try {
            log.info("Updating details: {}", detailsDTO);
            return ResponseEntity.ok(profileService.updateDetails(detailsDTO));
        } catch (Exception e) {
            log.error("Error updating details: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error updating details: " + e.getMessage());
        }
    }

    @GetMapping("profile/experience")
    public ResponseEntity<?> getExperience() {
        try {
            return ResponseEntity.ok(profileService.getExperience());
        } catch (Exception e) {
            log.error("Error retrieving experience: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving experience: " + e.getMessage());
        }
    }

    @PostMapping("profile/update-experience")
    public ResponseEntity<?> updateExperience(@RequestBody ExperienceDTO experienceDTO) {
        try {
            log.info("Updating experience: {}", experienceDTO);
            return ResponseEntity.ok(profileService.updateExperience(experienceDTO));
        } catch (Exception e) {
            log.error("Error updating experience: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error updating experience: " + e.getMessage());
        }
    }

    @GetMapping("profile/skills")
    public ResponseEntity<?> getSkills() {
        try {
            return ResponseEntity.ok(profileService.getSkills());
        } catch (Exception e) {
            log.error("Error retrieving skills: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving skills: " + e.getMessage());
        }
    }

    @PostMapping("profile/save-skills")
    public ResponseEntity<?> saveSkills(@RequestBody SkillsDTO skillsDTO) {
        try {
            log.info("Saving skills: {}", skillsDTO);
            return ResponseEntity.ok(profileService.saveSkills(skillsDTO));
        } catch (Exception e) {
            log.error("Error saving skills: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error saving skills: " + e.getMessage());
        }
    }

    @GetMapping("profile/projects")
    public ResponseEntity<?> getProjects() {
        try {
            return ResponseEntity.ok(profileService.getProjects());
        } catch (Exception e) {
            log.error("Error retrieving projects: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving projects: " + e.getMessage());
        }
    }

    @PostMapping("profile/update-projects")
    public ResponseEntity<?> updateProjects(@RequestBody ProjectDTO projectDTO) {
        try {
            log.info("Updating projects: {}", projectDTO);
            return ResponseEntity.ok(profileService.updateProjects(projectDTO));
        } catch (Exception e) {
            log.error("Error updating projects: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error updating projects: " + e.getMessage());
        }
    }

}
