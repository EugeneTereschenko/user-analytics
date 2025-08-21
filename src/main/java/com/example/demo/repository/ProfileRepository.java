package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p " +
            "FROM Profile p " +
            "JOIN UserProfile up ON p.id = up.profileId " +
            "WHERE up.userId = :userId")
    List<Profile> findProfilesByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.Image(i.id, i.name, i.data) " +
            "FROM Image i " +
            "JOIN ProfileImage pi ON i.id = pi.imageId " +
            "JOIN UserProfile up ON pi.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Image> findImagesByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.Project(pr.Id, pr.projectName, pr.dateFrom, pr.dateTo, pr.structure) " +
            "FROM Project pr " +
            "JOIN ProfileProject pp ON pr.Id = pp.projectId " +
            "JOIN UserProfile up ON pp.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.Education(e.id, e.universityName, e.dateFrom, e.dateTo, e.countryCity, e.degree) " +
            "FROM Education e " +
            "JOIN ProfileEducation pe ON e.id = pe.educationId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Education> findEducationByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.Details(d.id, d.notification, d.staff, d.bio, d.message) " +
            "FROM Details d " +
            "JOIN ProfileDetails pd ON d.id = pd.detailsId " +
            "JOIN UserProfile up ON pd.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Details> findDetailsByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.Experience(e.Id, e.roleName, e.dateFrom, e.dateTo, e.companyName, e.countryCity, e.service) " +
            "FROM Experience e " +
            "JOIN ProfileExperience pe ON e.Id = pe.experienceId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Experience> findExperienceByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.Skills(s.Id, s.programmingLanguages, s.webFrameworks, s.devOps, s.sql, s.vcs, s.tools) " +
            "FROM Skills s " +
            "JOIN ProfileSkills ps ON s.id = ps.skillsId " +
            "JOIN UserProfile up ON ps.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Skills> findSkillsByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.Certificate(c.Id, c.certificateName, c.dateFrom, c.dateTo, c.countryCity, c.institutionName, c.certificateUrl) " +
            "FROM Certificate c " +
            "JOIN ProfileCertificate pc ON c.Id = pc.certificateId " +
            "JOIN UserProfile up ON pc.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Certificate> findCertificatesByUserId(@Param("userId") Long userId);

}
