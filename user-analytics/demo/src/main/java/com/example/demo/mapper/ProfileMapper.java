package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    // Profile mappings
    public ProfileDTO toProfileDTO(Profile profile, User user) {
        if (profile == null || user == null) {
            return null;
        }

        return new ProfileDTO.Builder()
                .email(user.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .linkedin(profile.getLinkedin())
                .skype(profile.getSkype())
                .github(profile.getGithub())
                .address(profile.getAddress())
                .shippingAddress(profile.getShippingAddress())
                .phone(profile.getPhoneNumber())
                .build();
    }

    public ProfileDTO toProfileDTOWithoutProjects(Profile profile, User user) {
        if (profile == null || user == null) {
            return null;
        }

        return new ProfileDTO.Builder()
                .email(user.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phone(profile.getPhoneNumber())
                .linkedin(profile.getLinkedin())
                .skype(profile.getSkype())
                .github(profile.getGithub())
                .build();
    }

    public ProfileDTO toProfileDTOWithProjects(Profile profile, User user,
                                               Project recentProject, Project mostViewedProject) {
        if (profile == null || user == null) {
            return null;
        }

        return new ProfileDTO.Builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .email(user.getEmail())
                .phone(profile.getPhoneNumber())
                .linkedin(profile.getLinkedin())
                .skype(profile.getSkype())
                .github(profile.getGithub())
                .recentProject(recentProject != null ? recentProject.getProjectName() : null)
                .mostViewedProject(mostViewedProject != null ? mostViewedProject.getProjectName() : null)
                .build();
    }

    public void updateProfileFromDTO(Profile profile, ProfileDTO dto) {
        if (profile == null || dto == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            profile.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            profile.setLastName(dto.getLastName());
        }
        if (dto.getAddress() != null) {
            profile.setAddress(dto.getAddress());
        }
        if (dto.getShippingAddress() != null) {
            profile.setShippingAddress(dto.getShippingAddress());
        }
        if (dto.getPhone() != null) {
            profile.setPhoneNumber(dto.getPhone());
        }
        if (dto.getLinkedin() != null) {
            profile.setLinkedin(dto.getLinkedin());
        }
        if (dto.getSkype() != null) {
            profile.setSkype(dto.getSkype());
        }
        if (dto.getGithub() != null) {
            profile.setGithub(dto.getGithub());
        }
    }

    // Education mappings
    public EducationDTO toEducationDTO(Education education) {
        if (education == null) {
            return null;
        }

        return new EducationDTO.Builder()
                .universityName(education.getUniversityName())
                .dateFrom(education.getDateFrom())
                .dateTo(education.getDateTo())
                .countryCity(education.getCountryCity())
                .degree(education.getDegree())
                .build();
    }

    public void updateEducationFromDTO(Education education, EducationDTO dto) {
        if (education == null || dto == null) {
            return;
        }

        if (dto.getUniversityName() != null) {
            education.setUniversityName(dto.getUniversityName());
        }
        if (dto.getDateFrom() != null) {
            education.setDateFrom(dto.getDateFrom());
        }
        if (dto.getDateTo() != null) {
            education.setDateTo(dto.getDateTo());
        }
        if (dto.getCountryCity() != null) {
            education.setCountryCity(dto.getCountryCity());
        }
        if (dto.getDegree() != null) {
            education.setDegree(dto.getDegree());
        }
    }

    // Details mappings
    public DetailsDTO toDetailsDTO(Details details) {
        if (details == null) {
            return null;
        }

        return new DetailsDTO.Builder()
                .notification(String.valueOf(details.getNotification()))
                .staff(details.getStaff())
                .bio(details.getBio())
                .message(details.getMessage())
                .build();
    }

    public void updateDetailsFromDTO(Details details, DetailsDTO dto) {
        if (details == null || dto == null) {
            return;
        }

        if (dto.getNotification() != null) {
            details.setNotification(Boolean.parseBoolean(dto.getNotification()));
        }
        if (dto.getStaff() != null) {
            details.setStaff(dto.getStaff());
        }
        if (dto.getBio() != null) {
            details.setBio(dto.getBio());
        }
        if (dto.getMessage() != null) {
            details.setMessage(dto.getMessage());
        }
    }

    // Experience mappings
    public ExperienceDTO toExperienceDTO(Experience experience) {
        if (experience == null) {
            return null;
        }

        return new ExperienceDTO.Builder()
                .roleName(experience.getRoleName())
                .dateFrom(experience.getDateFrom())
                .dateTo(experience.getDateTo())
                .companyName(experience.getCompanyName())
                .countryCity(experience.getCountryCity())
                .service(experience.getService())
                .build();
    }

    public void updateExperienceFromDTO(Experience experience, ExperienceDTO dto) {
        if (experience == null || dto == null) {
            return;
        }

        if (dto.getRoleName() != null) {
            experience.setRoleName(dto.getRoleName());
        }
        if (dto.getDateFrom() != null) {
            experience.setDateFrom(dto.getDateFrom());
        }
        if (dto.getDateTo() != null) {
            experience.setDateTo(dto.getDateTo());
        }
        if (dto.getCompanyName() != null) {
            experience.setCompanyName(dto.getCompanyName());
        }
        if (dto.getCountryCity() != null) {
            experience.setCountryCity(dto.getCountryCity());
        }
        if (dto.getService() != null) {
            experience.setService(dto.getService());
        }
    }

    // Skills mappings
    public SkillsDTO toSkillsDTO(Skills skills) {
        if (skills == null) {
            return null;
        }

        return new SkillsDTO.Builder()
                .programmingLanguages(skills.getProgrammingLanguages())
                .webFrameworks(skills.getWebFrameworks())
                .devOps(skills.getDevOps())
                .sql(skills.getSql())
                .vcs(skills.getVcs())
                .tools(skills.getTools())
                .build();
    }

    public void updateSkillsFromDTO(Skills skills, SkillsDTO dto) {
        if (skills == null || dto == null) {
            return;
        }

        if (dto.getProgrammingLanguages() != null) {
            skills.setProgrammingLanguages(dto.getProgrammingLanguages());
        }
        if (dto.getWebFrameworks() != null) {
            skills.setWebFrameworks(dto.getWebFrameworks());
        }
        if (dto.getDevOps() != null) {
            skills.setDevOps(dto.getDevOps());
        }
        if (dto.getSql() != null) {
            skills.setSql(dto.getSql());
        }
        if (dto.getVcs() != null) {
            skills.setVcs(dto.getVcs());
        }
        if (dto.getTools() != null) {
            skills.setTools(dto.getTools());
        }
    }

    // Project mappings
    public ProjectDTO toProjectDTO(Project project) {
        if (project == null) {
            return null;
        }

        return new ProjectDTO.Builder()
                .projectName(project.getProjectName())
                .dateFrom(project.getDateFrom())
                .dateTo(project.getDateTo())
                .structure(project.getStructure())
                .build();
    }

    public void updateProjectFromDTO(Project project, ProjectDTO dto) {
        if (project == null || dto == null) {
            return;
        }

        if (dto.getProjectName() != null) {
            project.setProjectName(dto.getProjectName());
        }
        if (dto.getDateFrom() != null) {
            project.setDateFrom(dto.getDateFrom());
        }
        if (dto.getDateTo() != null) {
            project.setDateTo(dto.getDateTo());
        }
        if (dto.getStructure() != null) {
            project.setStructure(dto.getStructure());
        }
    }

    // Certificate mappings
    public CertificateDTO toCertificateDTO(Certificate certificate) {
        if (certificate == null) {
            return null;
        }

        return new CertificateDTO.Builder()
                .certificateName(certificate.getCertificateName())
                .dateFrom(certificate.getDateFrom())
                .dateTo(certificate.getDateTo())
                .build();
    }

    public void updateCertificateFromDTO(Certificate certificate, CertificateDTO dto) {
        if (certificate == null || dto == null) {
            return;
        }

        if (dto.getCertificateName() != null) {
            certificate.setCertificateName(dto.getCertificateName());
        }
        if (dto.getDateFrom() != null) {
            certificate.setDateFrom(dto.getDateFrom());
        }
        if (dto.getDateTo() != null) {
            certificate.setDateTo(dto.getDateTo());
        }
    }
}