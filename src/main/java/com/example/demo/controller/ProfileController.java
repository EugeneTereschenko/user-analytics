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
            //return ResponseEntity.ok(profileService.getProfileInformation());
            return ResponseEntity.ok(new ProfileDTO.Builder()
                    .email("test@test.com")
                    .phone("1234567890")
                    .recentProject("Project A")
                    .mostViewedProject("Project B")
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving profile information: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving profile information: " + e.getMessage());
        }
    }

    @GetMapping("profile/certificate-dates")
    public ResponseEntity<?> getCertificateDates() {
        try {
            //return ResponseEntity.ok(profileService.getCertificateDates());
            return ResponseEntity.ok(new CertificateDTO.Builder()
                    .certificateName("Java Programming")
                    .dateFrom("2023-01-01")
                    .dateTo("2024-01-01")
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving certificate dates: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving certificate dates: " + e.getMessage());
        }
    }

    @GetMapping("profile/get-profile")
    public ResponseEntity<?> getProfile() {
        try {
            return ResponseEntity.ok(new ProfileDTO.Builder()
                    .email("test@test.com")
                            .firstName("John")
                            .lastName("Doe")
                            .linkedin("https://www.linkedin.com/in/johndoe")
                            .skype("johndoe.skype")
                            .github("www.github.com/johndoe")
                    .address("123 Main St, Example City, Country")
                    .shippingAddress("456 Secondary St, Example City, Country")
                    .phone("1234567890")
                    .recentProject("Project A")
                    .mostViewedProject("Project B")
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving profile: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving profile: " + e.getMessage());
        }
    }

    @GetMapping("profile/education")
    public ResponseEntity<?> getEducation() {
        try {
            return ResponseEntity.ok(new EducationDTO.Builder()
                    .universityName("Example University")
                    .dateFrom("2015-09-01")
                    .dateTo("2019-06-01")
                    .countryCity("Example City, Country")
                    .degree("Bachelor's Degree")
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving education: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving education: " + e.getMessage());
        }
    }

    @GetMapping("profile/details")
    public ResponseEntity<?> getDetails() {
        try {
            return ResponseEntity.ok(new DetailsDTO.Builder()
                    .notification("Enabled")
                    .staff("John Doe")
                    .bio("Software Developer with 5 years of experience.")
                    .message("Welcome to my profile!")
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving details: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving details: " + e.getMessage());
        }
    }

    @GetMapping("profile/experience")
    public ResponseEntity<?> getExperience() {
        try {
            return ResponseEntity.ok(new ExperienceDTO.Builder()
                    .roleName("Software Engineer")
                    .dateFrom("2020-01-01")
                    .dateTo("2023-01-01")
                    .companyName("Tech Company")
                    .countryCity("Example City, Country")
                    .service("Developed web applications.")
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving experience: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving experience: " + e.getMessage());
        }
    }

    @GetMapping("profile/skills")
    public ResponseEntity<?> getSkills() {
        try {
            return ResponseEntity.ok(new SkillsDTO.Builder()
                    .programmingLanguages("Java, Python, JavaScript")
                    .webFrameworks("Spring Boot, Angular")
                    .devOps("Docker, Kubernetes")
                    .sql("PostgreSQL, MySQL")
                    .vcs("Git")
                    .tools("IntelliJ IDEA, VS Code")
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving skills: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving skills: " + e.getMessage());
        }
    }

    @GetMapping("profile/projects")
    public ResponseEntity<?> getProjects() {
        try {
            return ResponseEntity.ok(new ProjectDTO.Builder()
                    .projectName("Project Management System")
                    .dateFrom("2021-01-01")
                    .dateTo("2022-01-01")
                    .structure("Microservices Architecture")
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving projects: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving projects: " + e.getMessage());
        }
    }

    @PostMapping("profile/update-dates")
    public ResponseEntity<?> updateDates(@RequestBody CertificateDTO certificateDTO) {
        log.info("Updating certificate dates: {}", certificateDTO);
        return ResponseEntity.ok("Certificate dates updated successfully");
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

    @PostMapping("profile/update-experience")
    public ResponseEntity<?> updateExperience(@RequestBody ExperienceDTO experienceDTO) {
        log.info("Updating experience: {}", experienceDTO);
        return ResponseEntity.ok("Experience updated successfully");
    }

    @PostMapping("profile/update-projects")
    public ResponseEntity<?> updateProjects(@RequestBody ProjectDTO projectDTO) {
        log.info("Updating projects: {}", projectDTO);
        return ResponseEntity.ok("Projects updated successfully");
    }

    @PostMapping("profile/save-skills")
    public ResponseEntity<?> saveSkills(@RequestBody SkillsDTO skillsDTO) {
        log.info("Saving skills: {}", skillsDTO);
        return ResponseEntity.ok("Skills saved successfully");
    }
}
