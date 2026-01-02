package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.*;
import com.example.demo.mapper.ProfileMapper;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private static final String SUCCESS_STATUS = "200";
    private static final String DEFAULT_DATE_FROM = "2020-01-01";
    private static final String DEFAULT_DATE_TO = "2023-01-01";

    private final UserService userService;
    private final ProfileMapper profileMapper;

    private final DetailsRepository detailsRepository;
    private final ProfileDetailsRepository profileDetailsRepository;
    private final EducationRepository educationRepository;
    private final ProfileEducationRepository profileEducationRepository;
    private final ExperienceRepository experienceRepository;
    private final ProfileExperienceRepository profileExperienceRepository;
    private final CertificateRepository certificateRepository;
    private final ProfileCertificateRepository profileCertificateRepository;
    private final SkillsRepository skillsRepository;
    private final ProfileSkillsRepository profileSkillsRepository;
    private final ProjectRepository projectRepository;
    private final ProfileProjectRepository profileProjectRepository;
    private final UserProfileRepository userProfileRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final ImageRepository imageRepository;


    @Transactional
    public Long uploadImageToDatabase(MultipartFile file) {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getOrCreateUserProfile(user.getId());

        try {
            Image savedImage = saveImage(file, profile.getId());
            log.info("Image uploaded successfully for user: {}", user.getUsername());
            return savedImage.getId();
        } catch (IOException e) {
            log.error("Error uploading image for user: {}", user.getUsername(), e);
            throw new ImageUploadException("Failed to upload image", e);
        }
    }

    @Transactional(readOnly = true)
    public byte[] getImageForUser() {
        User user = getAuthenticatedUserOrThrow();

        Optional<Image> latestImage = profileRepository.findLatestImageByUserId(user.getId());

        if (latestImage.isEmpty() || latestImage.get().getData() == null) {
            log.warn("No valid image data found for user: {}", user.getUsername());
            return null;
        }

        return latestImage.get().getData();
    }

    private Image saveImage(MultipartFile file, Long profileId) throws IOException {
        Image image = new Image.Builder()
                .name(file.getOriginalFilename())
                .data(file.getBytes())
                .build();

        Image savedImage = imageRepository.saveAndFlush(image);

        profileImageRepository.saveAndFlush(new ProfileImage.Builder()
                .profileId(profileId)
                .imageId(savedImage.getId())
                .build());

        return savedImage;
    }

    @Transactional
    public Profile getOrCreateUserProfile(Long userId) {
        return profileRepository.findLatestProfileByUserId(userId)
                .orElseGet(() -> createProfile(userId));
    }

    @Transactional
    public Profile createProfile(Long userId) {
        Profile profile = profileRepository.saveAndFlush(new Profile());

        UserProfile userProfile = userProfileRepository.saveAndFlush(
                new UserProfile.Builder()
                        .userId(userId)
                        .profileId(profile.getId())
                        .build()
        );

        log.info("Profile created successfully with ID: {} for user ID: {}", profile.getId(), userId);
        return profile;
    }

    @Transactional(readOnly = true)
    public ProfileDTO getProfile() {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        return profileMapper.toProfileDTO(profile, user);
    }

    @Transactional(readOnly = true)
    public ProfileDTO getProfileInformation() {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        Optional<Project> recentProject = profileRepository.findMostRecentProjectByUserId(user.getId());
        Optional<Project> mostViewedProject = profileRepository.findLatestProjectByUserId(user.getId());

        if (recentProject.isEmpty()) {
            log.warn("No projects found for user: {}", user.getUsername());
            return profileMapper.toProfileDTOWithoutProjects(profile, user);
        }

        return profileMapper.toProfileDTOWithProjects(
                profile,
                user,
                recentProject.orElse(null),
                mostViewedProject.orElse(null)
        );
    }

    @Transactional
    public ResponseDTO updateProfile(ProfileDTO profileDTO) {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        profileMapper.updateProfileFromDTO(profile, profileDTO);
        profileRepository.saveAndFlush(profile);

        log.info("Profile updated successfully for user: {}", user.getUsername());
        return createSuccessResponse("Profile updated successfully");
    }

    @Transactional(readOnly = true)
    public EducationDTO getEducation() {
        User user = getAuthenticatedUserOrThrow();

        Optional<Education> education = profileRepository.findLatestEducationByUserId(user.getId());

        if (education.isEmpty()) {
            log.info("No education found for user: {}, returning default", user.getUsername());
            return profileMapper.toEducationDTO(createDefaultEducation());
        }

        return profileMapper.toEducationDTO(education.get());
    }

    @Transactional
    public ResponseDTO updateEducation(EducationDTO educationDTO) {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        Education education = getOrCreateEducation(user.getId(), profile.getId());
        profileMapper.updateEducationFromDTO(education, educationDTO);

        Education savedEducation = educationRepository.saveAndFlush(education);
        linkEducationToProfile(profile.getId(), savedEducation.getId());

        log.info("Education updated successfully for user: {}", user.getUsername());
        return createSuccessResponse("Education updated successfully");
    }

    private Education getOrCreateEducation(Long userId, Long profileId) {
        return profileRepository.findLatestEducationByUserId(userId)
                .orElseGet(() -> createEducation(profileId));
    }

    @Transactional
    public Education createEducation(Long profileId) {
        Education education = createDefaultEducation();
        Education savedEducation = educationRepository.saveAndFlush(education);

        profileEducationRepository.saveAndFlush(
                new ProfileEducation.Builder()
                        .profileId(profileId)
                        .educationId(savedEducation.getId())
                        .build()
        );

        return savedEducation;
    }

    private void linkEducationToProfile(Long profileId, Long educationId) {
        ProfileEducation existingLink = profileEducationRepository
                .findByProfileIdAndEducationId(profileId, educationId);

        if (existingLink == null) {
            profileEducationRepository.saveAndFlush(
                    new ProfileEducation.Builder()
                            .profileId(profileId)
                            .educationId(educationId)
                            .build()
            );
        }
    }

    @Transactional(readOnly = true)
    public DetailsDTO getDetails() {
        User user = getAuthenticatedUserOrThrow();

        Optional<Details> details = profileRepository.findLatestDetailsByUserId(user.getId());

        if (details.isEmpty()) {
            log.info("No details found for user: {}, returning default", user.getUsername());
            return profileMapper.toDetailsDTO(createDefaultDetails());
        }

        return profileMapper.toDetailsDTO(details.get());
    }

    @Transactional
    public ResponseDTO updateDetails(DetailsDTO detailsDTO) {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        Details details = getOrCreateDetails(user.getId(), profile.getId());
        profileMapper.updateDetailsFromDTO(details, detailsDTO);

        detailsRepository.saveAndFlush(details);

        log.info("Details updated successfully for user: {}", user.getUsername());
        return createSuccessResponse("Details updated successfully");
    }

    private Details getOrCreateDetails(Long userId, Long profileId) {
        return profileRepository.findLatestDetailsByUserId(userId)
                .orElseGet(() -> {
                    Details details = createDefaultDetails();
                    detailsRepository.saveAndFlush(details);

                    profileDetailsRepository.saveAndFlush(
                            new ProfileDetails.Builder()
                                    .profileId(profileId)
                                    .detailsId(details.getId())
                                    .build()
                    );

                    return details;
                });
    }


    @Transactional(readOnly = true)
    public ExperienceDTO getExperience() {
        User user = getAuthenticatedUserOrThrow();

        Optional<Experience> experience = profileRepository.findLatestExperienceByUserId(user.getId());

        if (experience.isEmpty()) {
            log.info("No experience found for user: {}, returning default", user.getUsername());
            return profileMapper.toExperienceDTO(createDefaultExperience());
        }

        return profileMapper.toExperienceDTO(experience.get());
    }

    @Transactional
    public ResponseDTO updateExperience(ExperienceDTO experienceDTO) {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        Experience experience = getOrCreateExperience(user.getId(), profile.getId());
        profileMapper.updateExperienceFromDTO(experience, experienceDTO);

        experienceRepository.saveAndFlush(experience);

        log.info("Experience updated successfully for user: {}", user.getUsername());
        return createSuccessResponse("Experience updated successfully");
    }

    private Experience getOrCreateExperience(Long userId, Long profileId) {
        return profileRepository.findLatestExperienceByUserId(userId)
                .orElseGet(() -> {
                    Experience experience = createDefaultExperience();
                    experienceRepository.saveAndFlush(experience);

                    profileExperienceRepository.saveAndFlush(
                            new ProfileExperience.Builder()
                                    .profileId(profileId)
                                    .experienceId(experience.getId())
                                    .build()
                    );

                    return experience;
                });
    }

    @Transactional(readOnly = true)
    public SkillsDTO getSkills() {
        User user = getAuthenticatedUserOrThrow();

        Optional<Skills> skills = profileRepository.findLatestSkillsByUserId(user.getId());

        if (skills.isEmpty()) {
            log.info("No skills found for user: {}, returning default", user.getUsername());
            return profileMapper.toSkillsDTO(createDefaultSkills());
        }

        return profileMapper.toSkillsDTO(skills.get());
    }

    @Transactional
    public ResponseDTO saveSkills(SkillsDTO skillsDTO) {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        Skills skills = getOrCreateSkills(user.getId(), profile.getId());
        profileMapper.updateSkillsFromDTO(skills, skillsDTO);

        skillsRepository.saveAndFlush(skills);

        log.info("Skills saved successfully for user: {}", user.getUsername());
        return createSuccessResponse("Skills saved successfully");
    }

    private Skills getOrCreateSkills(Long userId, Long profileId) {
        return profileRepository.findLatestSkillsByUserId(userId)
                .orElseGet(() -> {
                    Skills skills = createDefaultSkills();
                    skillsRepository.saveAndFlush(skills);

                    profileSkillsRepository.saveAndFlush(
                            new ProfileSkills.Builder()
                                    .profileId(profileId)
                                    .skillsId(skills.getId())
                                    .build()
                    );

                    return skills;
                });
    }


    @Transactional(readOnly = true)
    public ProjectDTO getProjects() {
        User user = getAuthenticatedUserOrThrow();

        Optional<Project> project = profileRepository.findLatestProjectByUserId(user.getId());

        if (project.isEmpty()) {
            log.info("No projects found for user: {}, returning default", user.getUsername());
            return profileMapper.toProjectDTO(createDefaultProject());
        }

        return profileMapper.toProjectDTO(project.get());
    }

    @Transactional
    public ResponseDTO updateProjects(ProjectDTO projectDTO) {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        Project project = getOrCreateProject(user.getId(), profile.getId());
        profileMapper.updateProjectFromDTO(project, projectDTO);

        projectRepository.saveAndFlush(project);

        log.info("Projects updated successfully for user: {}", user.getUsername());
        return createSuccessResponse("Projects updated successfully");
    }

    private Project getOrCreateProject(Long userId, Long profileId) {
        return profileRepository.findLatestProjectByUserId(userId)
                .orElseGet(() -> {
                    Project project = createDefaultProject();
                    projectRepository.saveAndFlush(project);

                    profileProjectRepository.saveAndFlush(
                            new ProfileProject.Builder()
                                    .profileId(profileId)
                                    .projectId(project.getId())
                                    .build()
                    );

                    return project;
                });
    }

    @Transactional(readOnly = true)
    public CertificateDTO getCertificates() {
        User user = getAuthenticatedUserOrThrow();

        Optional<Certificate> certificate = profileRepository.findLatestCertificateByUserId(user.getId());

        if (certificate.isEmpty()) {
            log.info("No certificates found for user: {}, returning default", user.getUsername());
            return profileMapper.toCertificateDTO(createDefaultCertificate());
        }

        return profileMapper.toCertificateDTO(certificate.get());
    }

    @Transactional
    public ResponseDTO updateCertificateDates(CertificateDTO certificateDTO) {
        User user = getAuthenticatedUserOrThrow();
        Profile profile = getProfileByUserId(user.getId());

        Certificate certificate = getOrCreateCertificate(user.getId(), profile.getId());
        profileMapper.updateCertificateFromDTO(certificate, certificateDTO);

        certificateRepository.saveAndFlush(certificate);

        log.info("Certificate dates updated successfully for user: {}", user.getUsername());
        return createSuccessResponse("Certificate dates updated successfully");
    }

    private Certificate getOrCreateCertificate(Long userId, Long profileId) {
        return profileRepository.findLatestCertificateByUserId(userId)
                .orElseGet(() -> {
                    Certificate certificate = createDefaultCertificate();
                    certificateRepository.saveAndFlush(certificate);

                    profileCertificateRepository.saveAndFlush(
                            new ProfileCertificate.Builder()
                                    .profileId(profileId)
                                    .certificateId(certificate.getId())
                                    .build()
                    );

                    return certificate;
                });
    }

    private User getAuthenticatedUserOrThrow() {
        return userService.getAuthenticatedUser()
                .orElseThrow(() -> new UserNotAuthenticatedException());
    }

    private Profile getProfileByUserId(Long userId) {
        return profileRepository.findLatestProfileByUserId(userId)
                .orElseGet(() -> {
                    log.info("No profile found for user ID: {}, creating new profile", userId);
                    return createProfile(userId);
                });
    }

    private ResponseDTO createSuccessResponse(String message) {
        return new ResponseDTO.Builder()
                .message(message)
                .status(SUCCESS_STATUS)
                .data(true)
                .build();
    }


    private Education createDefaultEducation() {
        return new Education.Builder()
                .universityName("Example University")
                .dateFrom("2015-09-01")
                .dateTo("2019-06-01")
                .countryCity("Example City, Country")
                .degree("Bachelor's Degree")
                .build();
    }

    private Details createDefaultDetails() {
        return new Details.Builder()
                .notification(false)
                .staff("Default Staff")
                .bio("Default Bio")
                .message("Default Message")
                .build();
    }

    private Experience createDefaultExperience() {
        return new Experience.Builder()
                .roleName("Software Engineer")
                .dateFrom(DEFAULT_DATE_FROM)
                .dateTo(DEFAULT_DATE_TO)
                .companyName("Tech Company")
                .countryCity("Example City, Country")
                .service("Developed web applications.")
                .build();
    }

    private Skills createDefaultSkills() {
        return new Skills.Builder()
                .programmingLanguages("Java, Python, JavaScript")
                .webFrameworks("Spring Boot, Angular")
                .devOps("Docker, Kubernetes")
                .sql("PostgreSQL, MySQL")
                .vcs("Git")
                .tools("IntelliJ IDEA, VS Code")
                .build();
    }

    private Project createDefaultProject() {
        return new Project.Builder()
                .projectName("Project Management System")
                .dateFrom("2021-01-01")
                .dateTo("2022-01-01")
                .structure("Microservices Architecture")
                .build();
    }

    private Certificate createDefaultCertificate() {
        return new Certificate.Builder()
                .certificateName("Java Programming")
                .dateFrom("2023-01-01")
                .dateTo("2024-01-01")
                .build();
    }
}