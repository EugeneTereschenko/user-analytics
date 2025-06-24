package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserService userService;
    private final DetailsRepository detailsRepository;
    private final ProfileDetailsRepository profileDetailsRepository;
    private final EducationRepository educationRepository;
    private final ProfileEducationRepository profileEducationRepository;
    private final CardRepository cardRepository;
    private final UserProfileRepository userProfileRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final ImageRepository imageRepository;

    public String uploadImageToDatabase(MultipartFile file) {
        try {
            Optional<User> user = userService.getAuthenticatedUser();
            List<Profile> profiles = profileRepository.findProfilesByUserId(user.get().getUserId());
            if (user.isPresent() && profiles.isEmpty()) {
                log.info("User {} has no profile, creating a new one.", user.get().getUsername());
                Profile profile = createProfile(user.get().getUserId());
                return String.valueOf(saveImage(file, profile.getId()).getId());
            }
            if (user.isPresent() && !profiles.isEmpty()) {
                log.info("User {} has existing profiles, saving image to the first profile.", user.get().getUsername());
                return String.valueOf(saveImage(file, profiles.stream().reduce((first, second) -> second).get().getId()).getId());
            }
        } catch (Exception e) {
            log.error("Error uploading image: {}", e.getMessage());
            return "Error uploading image: " + e.getMessage();
        }
        return "";
    }

    @Transactional
    public byte[] getImageForUser() {
        try {
            Optional<User> user = userService.getAuthenticatedUser();
            if (user.isEmpty()) {
                log.warn("No authenticated user found.");
                return null;
            }

            List<Image> images = profileRepository.findImagesByUserId(user.get().getUserId());
            if (images.isEmpty()) {
                log.warn("No images found for user: {}", user.get().getUsername());
                return null;
            }

            Image lastImage = images.stream().reduce((first, second) -> second).orElse(null);
            if (lastImage == null || lastImage.getData() == null) {
                log.warn("No valid image data found for user: {}", user.get().getUsername());
                return null;
            }

            return lastImage.getData();
        } catch (Exception e) {
            log.error("Error retrieving image for user: {}", e.getMessage(), e);
            return null;
        }
    }

    @Transactional
    private Image saveImage(MultipartFile file, Long profileId) throws IOException {

        Image existingImage = imageRepository.saveAndFlush(new Image.Builder()
                .name(file.getOriginalFilename())
                .data(file.getBytes())
                .build());

        if (existingImage == null) {
            log.error("Failed to save image for profile ID: {}", profileId);
            throw new RuntimeException("Failed to save image for profile ID: " + profileId);

        }
        profileImageRepository.saveAndFlush(new ProfileImage.Builder()
                .profileId(profileId)
                .imageId(existingImage.getId())
                .build());
        return existingImage;
    }

    @Transactional
    public Profile createProfile(Long userId) {
        Profile existsProfile = profileRepository.saveAndFlush(new Profile());
        UserProfile existsUserProfile = userProfileRepository.saveAndFlush(new UserProfile.Builder()
                .userId(userId)
                .profileId(existsProfile.getId())
                .build());
        if (existsUserProfile != null) {
            return existsProfile;
        }
        log.error("Failed to create profile for user ID: {}", userId);
        throw new RuntimeException("Failed to create profile for user ID: " + userId);
    }

    @Transactional
    public ProfileDTO getProfileInformation() {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile profile = getProfileByUser(user);

        List<Project> projects = profileRepository.findProjectsByUserId(user.get().getUserId());
        if (projects.isEmpty()) {
            log.warn("No projects found for user: {}", user.get().getUsername());
            throw new RuntimeException("No projects found for user: " + user.get().getUsername());
        }
        Project recentProject = projects.stream().reduce((first, second) -> second).get(); // Assuming the last project is used
        if (recentProject.getProjectName() == null) {
            log.warn("No recent project found for user: {}", user.get().getUsername());
            throw new RuntimeException("No recent project found for user: " + user.get().getUsername());
        }
        Project mostViewedProject = projects.stream().reduce((first, second) -> second).get();; // Assuming the first project is the most viewed
        return new ProfileDTO.Builder()
                .email(user.get().getEmail())
                .phone(profile.getPhoneNumber())
                .recentProject(recentProject.getProjectName())
                .mostViewedProject(mostViewedProject.getProjectName())
                .build();
    }


    @Transactional
    public ResponseDTO updateProfile(ProfileDTO profileDTO) {

        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile profile = getProfileByUser(user);

        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName(profileDTO.getLastName());
        profile.setAddress(profileDTO.getAddress());
        profile.setShippingAddress(profileDTO.getShippingAddress());
        profile.setPhoneNumber(profileDTO.getPhone());
        profileRepository.saveAndFlush(profile);

        log.info("Profile updated successfully for user: {}", user.get().getUsername());
        return new ResponseDTO("\"Profile updated successfully\"", "200", "true");
    }


    @Transactional
    public EducationDTO getEducation() {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        List<Education> educationList = profileRepository.findEducationByUserId(user.get().getUserId());
        if (educationList.isEmpty()) {
            log.warn("No education records found for user: {}", user.get().getUsername());
            throw new RuntimeException("No education records found for user: " + user.get().getUsername());
        }

        Education education = educationList.stream().reduce((first, second) -> second).get();; // Assuming the first education record is used
        return new EducationDTO.Builder()
                .universityName(education.getUniversityName())
                .dateFrom(education.getDateFrom())
                .dateTo(education.getDateTo())
                .countryCity(education.getCountryCity())
                .degree(education.getDegree())
                .build();
    }

    @Transactional
    public ResponseDTO updateEducation(EducationDTO educationDTO) {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile existsProfile = getProfileByUser(user);

        List<Education> educationList = profileRepository.findEducationByUserId(user.get().getUserId());
        Education education = null;
        if (educationList.isEmpty()) {
            education = createEducation(existsProfile.getId());
            if (education == null) {
                log.error("Failed to create education for user: {}", user.get().getUsername());
                throw new RuntimeException("Failed to create education for user: " + user.get().getUsername());
            }
        } else {
            education = educationList.stream().reduce((first, second) -> second).get(); // Assuming the last education record is used
        }

        education.setUniversityName(educationDTO.getUniversityName());
        education.setDateFrom(educationDTO.getDateFrom());
        education.setDateTo(educationDTO.getDateTo());
        education.setCountryCity(educationDTO.getCountryCity());
        education.setDegree(educationDTO.getDegree());
        Education existsEducation = educationRepository.saveAndFlush(education);


        ProfileEducation existingProfileEducation = profileEducationRepository.findByProfileIdAndEducationId(existsProfile.getId(), existsEducation.getId());
        if (existingProfileEducation != null) {
            log.info("Updating existing education for profile ID: {}", existsProfile.getId());
            existingProfileEducation.setEducationId(existsEducation.getId());
            profileEducationRepository.saveAndFlush(existingProfileEducation);
        } else {
            ProfileEducation profileEducation = new ProfileEducation.Builder()
                    .profileId(existsProfile.getId())
                    .educationId(existsEducation.getId())
                    .build();
            profileEducationRepository.saveAndFlush(profileEducation);
        }

        log.info("Education updated successfully for user: {}", user.get().getUsername());
        return new ResponseDTO.Builder()
                .message("Education updated successfully")
                .status("200")
                .data(true)
                .build();
    }


    @Transactional
    public Education createEducation(Long profileId) {
        Education education = new Education.Builder()
                .universityName("Default University")
                .dateFrom("2020-01-01")
                .dateTo("2024-01-01")
                .countryCity("Default City")
                .degree("Bachelor's Degree")
                .build();

        Education savedEducation = educationRepository.saveAndFlush(education);
        if (savedEducation == null) {
            log.error("Failed to create education for profile ID: {}", profileId);
            throw new RuntimeException("Failed to create education for profile ID: " + profileId);
        }

        ProfileEducation profileEducation = new ProfileEducation.Builder()
                .profileId(profileId)
                .educationId(savedEducation.getId())
                .build();
        profileEducationRepository.saveAndFlush(profileEducation);

        return savedEducation;

    }

    @Transactional
    public DetailsDTO getDetails() {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        List<Details> details = profileRepository.findDetailsByUserId(user.get().getUserId());
        if (details.isEmpty()) {
            log.warn("No details found for user: {}", user.get().getUsername());
            throw new RuntimeException("No details found for user: " + user.get().getUsername());
        }

        Details excitsDetails = details.stream().reduce((first, second) -> second).get(); // Assuming the first details record is used

        return new DetailsDTO.Builder()
                .notification(String.valueOf(excitsDetails.getNotification()))
                .staff(excitsDetails.getStaff())
                .bio(excitsDetails.getBio())
                .message(excitsDetails.getMessage())
                .build();
    }

    @Transactional
    public ResponseDTO updateDetails(DetailsDTO detailsDTO) {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile profile = getProfileByUser(user);

        List<Details> detailsList = profileRepository.findDetailsByUserId(user.get().getUserId());
        Details details = null;
        if (detailsList.isEmpty()) {
            details = new Details.Builder()
                    .notification(false)
                    .staff("Default Staff")
                    .bio("Default Bio")
                    .message("Default Message")
                    .build();
            detailsRepository.saveAndFlush(details);

            ProfileDetails profileDetails = new ProfileDetails.Builder()
                    .profileId(profile.getId())
                    .detailsId(details.getId())
                    .build();
            profileDetailsRepository.saveAndFlush(profileDetails);
        } else {
            details = detailsList.stream().reduce((first, second) -> second).get(); // Assuming the last details record is used
        }

        details.setNotification(Boolean.parseBoolean(detailsDTO.getNotification()));
        details.setStaff(detailsDTO.getStaff());
        details.setBio(detailsDTO.getBio());
        details.setMessage(detailsDTO.getMessage());
        detailsRepository.saveAndFlush(details);

        log.info("Details updated successfully for user: {}", user.get().getUsername());
        return new ResponseDTO.Builder()
                .message("Details updated successfully")
                .status("200")
                .data(true)
                .build();
    }

    @Transactional
    public ExperienceDTO getExperience() {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        List<Experience> experiences = profileRepository.findExperienceByUserId(user.get().getUserId());
        if (experiences.isEmpty()) {
            log.warn("No experience records found for user: {}", user.get().getUsername());
            throw new RuntimeException("No experience records found for user: " + user.get().getUsername());
        }

        Experience experience = experiences.stream().reduce((first, second) -> second).get(); // Assuming the first experience record is used
        return new ExperienceDTO.Builder()
                .roleName(experience.getRoleName())
                .dateFrom(experience.getDateFrom())
                .dateTo(experience.getDateTo())
                .companyName(experience.getCompanyName())
                .countryCity(experience.getCountryCity())
                .service(experience.getService())
                .build();
    }

    @Transactional
    public SkillsDTO getSkills() {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        List<Skills> skills = profileRepository.findSkillsByUserId(user.get().getUserId());
        if (skills.isEmpty()) {
            log.warn("No skills found for user: {}", user.get().getUsername());
            throw new RuntimeException("No skills found for user: " + user.get().getUsername());
        }

        Skills skill = skills.stream().reduce((first, second) -> second).get(); // Assuming the first skills record is used

        return new SkillsDTO.Builder()
                .programmingLanguages(skill.getProgrammingLanguages())
                .webFrameworks(skill.getWebFrameworks())
                .devOps(skill.getDevOps())
                .sql(skill.getSql())
                .vcs(skill.getVcs())
                .tools(skill.getTools())
                .build();
    }

    @Transactional
    public ProjectDTO getProjects() {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        List<Project> projects = profileRepository.findProjectsByUserId(user.get().getUserId());
        if (projects.isEmpty()) {
            log.warn("No projects found for user: {}", user.get().getUsername());
            throw new RuntimeException("No projects found for user: " + user.get().getUsername());
        }

        Project project = projects.get(0); // Assuming the first project is used
        return new ProjectDTO.Builder()
                .projectName(project.getProjectName())
                .dateFrom(project.getDateFrom())
                .dateTo(project.getDateTo())
                .structure(project.getStructure())
                .build();
    }

    private Profile getProfileByUser(Optional<User> user) {
        List<Profile> profiles = profileRepository.findProfilesByUserId(user.get().getUserId());
        Profile profile = null;
        if (profiles.isEmpty()) {
            log.warn("No profiles found for user: {}", user.get().getUsername());
            profile = createProfile(user.get().getUserId());
            if (profile == null) {
                log.error("Failed to create profile for user: {}", user.get().getUsername());
                throw new RuntimeException("Failed to create profile for user: " + user.get().getUsername());
            }
        } else {
            profile = profiles.stream().reduce((first, second) -> second).get(); // Assuming the last profile is used
        }
        return profile;
    }

}
