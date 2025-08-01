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


@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserService userService;
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
    public ProfileDTO getProfile() {

        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile profile = getProfileByUser(user);
        if (profile == null) {
            log.warn("No profile found for user: {}", user.get().getUsername());
            //throw new RuntimeException("No profile found for user: " + user.get().getUsername());
            return new ProfileDTO.Builder()
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
                    .build();
        }

        return new ProfileDTO.Builder()
                .email(user.get().getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .linkedin("https://www.linkedin.com/in/johndoe")
                .skype("johndoe.skype")
                .github("www.github.com/johndoe")
                .address(profile.getAddress())
                .shippingAddress(profile.getShippingAddress())
                .phone(profile.getPhoneNumber())
                .recentProject("Project A")
                .mostViewedProject("Project B")
                .build();
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
            //throw new RuntimeException("No projects found for user: " + user.get().getUsername());
            return new ProfileDTO.Builder()
                    .email("test@test.com")
                    .firstName("John")
                    .lastName("Doe")
                    .email("test@test.com")
                    .phone("1234567890")
                    .recentProject("Project A")
                    .mostViewedProject("Project B")
                    .build();
        }
        Project recentProject = projects.stream().reduce((first, second) -> second).get(); // Assuming the last project is used
        if (recentProject.getProjectName() == null) {
            log.warn("No recent project found for user: {}", user.get().getUsername());
            //throw new RuntimeException("No recent project found for user: " + user.get().getUsername());
            return new ProfileDTO.Builder()
                    .email("test@test.com")
                    .firstName("John")
                    .lastName("Doe")
                    .email("test@test.com")
                    .phone("1234567890")
                    .recentProject("Project A")
                    .mostViewedProject("Project B")
                    .build();
        }
        Project mostViewedProject = projects.stream().reduce((first, second) -> second).get();; // Assuming the first project is the most viewed
        return new ProfileDTO.Builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
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
            //throw new RuntimeException("No education records found for user: " + user.get().getUsername());
            return new EducationDTO.Builder()
                    .universityName("Example University")
                    .dateFrom("2015-09-01")
                    .dateTo("2019-06-01")
                    .countryCity("Example City, Country")
                    .degree("Bachelor's Degree")
                    .build();
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
    public DetailsDTO getDetails() {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        List<Details> details = profileRepository.findDetailsByUserId(user.get().getUserId());
        if (details.isEmpty()) {
            log.warn("No details found for user: {}", user.get().getUsername());
            //throw new RuntimeException("No details found for user: " + user.get().getUsername());
            return new DetailsDTO.Builder()
                    .notification("Enabled")
                    .staff("John Doe")
                    .bio("Software Developer with 5 years of experience.")
                    .message("Welcome to my profile!")
                    .build();
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
            //throw new RuntimeException("No experience records found for user: " + user.get().getUsername());
            return new ExperienceDTO.Builder()
                    .roleName("Software Engineer")
                    .dateFrom("2020-01-01")
                    .dateTo("2023-01-01")
                    .companyName("Tech Company")
                    .countryCity("Example City, Country")
                    .service("Developed web applications.")
                    .build();
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
    public ResponseDTO updateExperience(ExperienceDTO experienceDTO) {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile profile = getProfileByUser(user);

        List<Experience> experienceList = profileRepository.findExperienceByUserId(user.get().getUserId());
        Experience experience = null;
        if (experienceList.isEmpty()) {
            experience = new Experience.Builder()
                    .roleName("Default Role")
                    .dateFrom("2020-01-01")
                    .dateTo("2023-01-01")
                    .companyName("Default Company")
                    .countryCity("Default City")
                    .service("Default Service")
                    .build();
            experienceRepository.saveAndFlush(experience);

            ProfileExperience profileExperience = new ProfileExperience.Builder()
                    .profileId(profile.getId())
                    .experienceId(experience.getId())
                    .build();
            profileExperienceRepository.saveAndFlush(profileExperience);
        } else {
            experience = experienceList.stream().reduce((first, second) -> second).get(); // Assuming the last experience record is used
        }

        experience.setRoleName(experienceDTO.getRoleName());
        experience.setDateFrom(experienceDTO.getDateFrom());
        experience.setDateTo(experienceDTO.getDateTo());
        experience.setCompanyName(experienceDTO.getCompanyName());
        experience.setCountryCity(experienceDTO.getCountryCity());
        experience.setService(experienceDTO.getService());
        experienceRepository.saveAndFlush(experience);

        log.info("Experience updated successfully for user: {}", user.get().getUsername());
        return new ResponseDTO.Builder()
                .message("Experience updated successfully")
                .status("200")
                .data(true)
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
            //throw new RuntimeException("No skills found for user: " + user.get().getUsername());
            return new SkillsDTO.Builder()
                    .programmingLanguages("Java, Python, JavaScript")
                    .webFrameworks("Spring Boot, Angular")
                    .devOps("Docker, Kubernetes")
                    .sql("PostgreSQL, MySQL")
                    .vcs("Git")
                    .tools("IntelliJ IDEA, VS Code")
                    .build();
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
    public ResponseDTO saveSkills(SkillsDTO skillsDTO) {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile profile = getProfileByUser(user);

        List<Skills> skillsList = profileRepository.findSkillsByUserId(user.get().getUserId());
        Skills skills = null;
        if (skillsList.isEmpty()) {
            skills = new Skills.Builder()
                    .programmingLanguages("Default Programming Languages")
                    .webFrameworks("Default Web Frameworks")
                    .devOps("Default DevOps")
                    .sql("Default SQL")
                    .vcs("Default VCS")
                    .tools("Default Tools")
                    .build();
            skillsRepository.saveAndFlush(skills);

            ProfileSkills profileSkills = new ProfileSkills.Builder()
                    .profileId(profile.getId())
                    .skillsId(skills.getId())
                    .build();
            profileSkillsRepository.saveAndFlush(profileSkills);
        } else {
            skills = skillsList.stream().reduce((first, second) -> second).get(); // Assuming the last skills record is used
        }

        skills.setProgrammingLanguages(skillsDTO.getProgrammingLanguages());
        skills.setWebFrameworks(skillsDTO.getWebFrameworks());
        skills.setDevOps(skillsDTO.getDevOps());
        skills.setSql(skillsDTO.getSql());
        skills.setVcs(skillsDTO.getVcs());
        skills.setTools(skillsDTO.getTools());
        skillsRepository.saveAndFlush(skills);

        log.info("Skills saved successfully for user: {}", user.get().getUsername());
        return new ResponseDTO.Builder()
                .message("Skills saved successfully")
                .status("200")
                .data(true)
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
            return new ProjectDTO.Builder()
                    .projectName("Project Management System")
                    .dateFrom("2021-01-01")
                    .dateTo("2022-01-01")
                    .structure("Microservices Architecture")
                    .build();
            //throw new RuntimeException("No projects found for user: " + user.get().getUsername());
        }
        Project project = projects.stream().reduce((first, second) -> second).get(); // Assuming the first project is used
        return new ProjectDTO.Builder()
                .projectName(project.getProjectName())
                .dateFrom(project.getDateFrom())
                .dateTo(project.getDateTo())
                .structure(project.getStructure())
                .build();
    }
    @Transactional
    public ResponseDTO updateProjects(ProjectDTO projectDTO) {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile profile = getProfileByUser(user);

        List<Project> projectList = profileRepository.findProjectsByUserId(user.get().getUserId());
        Project project = null;
        if (projectList.isEmpty()) {
            project = new Project.Builder()
                    .projectName("Default Project")
                    .dateFrom("2020-01-01")
                    .dateTo("2023-01-01")
                    .structure("Default Structure")
                    .build();
            projectRepository.saveAndFlush(project);

            ProfileProject profileProject = new ProfileProject.Builder()
                    .profileId(profile.getId())
                    .projectId(project.getId())
                    .build();
            profileProjectRepository.saveAndFlush(profileProject);
        } else {
            project = projectList.stream().reduce((first, second) -> second).get(); // Assuming the last project record is used
        }

        project.setProjectName(projectDTO.getProjectName());
        project.setDateFrom(projectDTO.getDateFrom());
        project.setDateTo(projectDTO.getDateTo());
        project.setStructure(projectDTO.getStructure());
        projectRepository.saveAndFlush(project);

        log.info("Projects updated successfully for user: {}", user.get().getUsername());
        return new ResponseDTO.Builder()
                .message("Projects updated successfully")
                .status("200")
                .data(true)
                .build();
    }

    @Transactional
    public CertificateDTO getCertificates() {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        List<Certificate> certificates = profileRepository.findCertificatesByUserId(user.get().getUserId());
        if (certificates.isEmpty()) {
            log.warn("No certificates found for user: {}", user.get().getUsername());
            //throw new RuntimeException("No certificates found for user: " + user.get().getUsername());
            return new CertificateDTO.Builder()
                    .certificateName("Java Programming")
                    .dateFrom("2023-01-01")
                    .dateTo("2024-01-01")
                    .build();
        }

        Certificate certificate = certificates.stream().reduce((first, second) -> second).get(); // Assuming the first certificate record is used
        return new CertificateDTO.Builder()
                .certificateName(certificate.getCertificateName())
                .dateFrom(certificate.getDateFrom())
                .dateTo(certificate.getDateTo())
                .build();
    }

    @Transactional
    public ResponseDTO updateCertificateDates(CertificateDTO certificateDTO) {
        Optional<User> user = userService.getAuthenticatedUser();
        if (user.isEmpty()) {
            log.warn("No authenticated user found.");
            throw new RuntimeException("No authenticated user found.");
        }

        Profile profile = getProfileByUser(user);

        List<Certificate> certificateList = profileRepository.findCertificatesByUserId(user.get().getUserId());
        Certificate certificate = null;
        if (certificateList.isEmpty()) {
            certificate = new Certificate.Builder()
                    .certificateName("Default Certificate")
                    .dateFrom("2020-01-01")
                    .dateTo("2023-01-01")
                    .build();
            certificateRepository.saveAndFlush(certificate);

            ProfileCertificate profileCertificate = new ProfileCertificate.Builder()
                    .profileId(profile.getId())
                    .certificateId(certificate.getId())
                    .build();
            profileCertificateRepository.saveAndFlush(profileCertificate);
        } else {
            certificate = certificateList.stream().reduce((first, second) -> second).get(); // Assuming the last certificate record is used
        }

        certificate.setCertificateName(certificateDTO.getCertificateName());
        certificate.setDateFrom(certificateDTO.getDateFrom());
        certificate.setDateTo(certificateDTO.getDateTo());
        certificateRepository.saveAndFlush(certificate);

        log.info("Certificate dates updated successfully for user: {}", user.get().getUsername());
        return new ResponseDTO.Builder()
                .message("Certificate dates updated successfully")
                .status("200")
                .data(true)
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
}
