package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {



    /**
     * Find all profiles for a user
     */
    @Query("SELECT p " +
            "FROM Profile p " +
            "JOIN UserProfile up ON p.id = up.profileId " +
            "WHERE up.userId = :userId")
    List<Profile> findProfilesByUserId(@Param("userId") Long userId);

    /**
     * Find the latest (most recent) profile for a user
     * Optimized: Returns only one record instead of loading all
     */
    @Query("SELECT p " +
            "FROM Profile p " +
            "JOIN UserProfile up ON p.id = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY p.id DESC " +
            "LIMIT 1")
    Optional<Profile> findLatestProfileByUserId(@Param("userId") Long userId);

    /**
     * Check if a user has any profile
     * Optimized: Uses COUNT instead of loading entities
     */
    @Query("SELECT COUNT(p) > 0 FROM Profile p " +
            "JOIN UserProfile up ON up.profileId = p.id " +
            "WHERE up.userId = :userId")
    boolean existsProfileForUser(@Param("userId") Long userId);



    /**
     * Find all images for a user
     */
    @Query("SELECT new com.example.demo.model.Image(i.id, i.name, i.data) " +
            "FROM Image i " +
            "JOIN ProfileImage pi ON i.id = pi.imageId " +
            "JOIN UserProfile up ON pi.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Image> findImagesByUserId(@Param("userId") Long userId);

    /**
     * Find the latest image for a user
     * Optimized: Returns only the most recent image
     */
    @Query("SELECT new com.example.demo.model.Image(i.id, i.name, i.data) " +
            "FROM Image i " +
            "JOIN ProfileImage pi ON i.id = pi.imageId " +
            "JOIN UserProfile up ON pi.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY i.id DESC " +
            "LIMIT 1")
    Optional<Image> findLatestImageByUserId(@Param("userId") Long userId);

    /**
     * Find the latest image without loading the byte data (for metadata only)
     * Optimized: Excludes data field for performance
     */
    @Query("SELECT new com.example.demo.model.Image(i.id, i.name, null) " +
            "FROM Image i " +
            "JOIN ProfileImage pi ON i.id = pi.imageId " +
            "JOIN UserProfile up ON pi.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY i.id DESC " +
            "LIMIT 1")
    Optional<Image> findLatestImageMetadataByUserId(@Param("userId") Long userId);


    /**
     * Find all projects for a user
     */
    @Query("SELECT new com.example.demo.model.Project(pr.id, pr.projectName, pr.dateFrom, pr.dateTo, pr.structure) " +
            "FROM Project pr " +
            "JOIN ProfileProject pp ON pr.id = pp.projectId " +
            "JOIN UserProfile up ON pp.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);

    /**
     * Find the latest project for a user
     * Optimized: Single record retrieval
     */
    @Query("SELECT new com.example.demo.model.Project(pr.id, pr.projectName, pr.dateFrom, pr.dateTo, pr.structure) " +
            "FROM Project pr " +
            "JOIN ProfileProject pp ON pr.id = pp.projectId " +
            "JOIN UserProfile up ON pp.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY pr.id DESC " +
            "LIMIT 1")
    Optional<Project> findLatestProjectByUserId(@Param("userId") Long userId);

    /**
     * Find the most recent project by date (not by ID)
     */
    @Query("SELECT new com.example.demo.model.Project(pr.id, pr.projectName, pr.dateFrom, pr.dateTo, pr.structure) " +
            "FROM Project pr " +
            "JOIN ProfileProject pp ON pr.id = pp.projectId " +
            "JOIN UserProfile up ON pp.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY pr.dateTo DESC, pr.id DESC " +
            "LIMIT 1")
    Optional<Project> findMostRecentProjectByUserId(@Param("userId") Long userId);


    /**
     * Find all education records for a user
     */
    @Query("SELECT new com.example.demo.model.Education(e.id, e.universityName, e.dateFrom, e.dateTo, e.countryCity, e.degree) " +
            "FROM Education e " +
            "JOIN ProfileEducation pe ON e.id = pe.educationId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Education> findEducationByUserId(@Param("userId") Long userId);

    /**
     * Find the latest education record for a user
     * Optimized: Single record retrieval
     */
    @Query("SELECT new com.example.demo.model.Education(e.id, e.universityName, e.dateFrom, e.dateTo, e.countryCity, e.degree) " +
            "FROM Education e " +
            "JOIN ProfileEducation pe ON e.id = pe.educationId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY e.id DESC " +
            "LIMIT 1")
    Optional<Education> findLatestEducationByUserId(@Param("userId") Long userId);


    /**
     * Find all details records for a user
     */
    @Query("SELECT new com.example.demo.model.Details(d.id, d.notification, d.staff, d.bio, d.message) " +
            "FROM Details d " +
            "JOIN ProfileDetails pd ON d.id = pd.detailsId " +
            "JOIN UserProfile up ON pd.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Details> findDetailsByUserId(@Param("userId") Long userId);

    /**
     * Find the latest details record for a user
     * Optimized: Single record retrieval
     */
    @Query("SELECT new com.example.demo.model.Details(d.id, d.notification, d.staff, d.bio, d.message) " +
            "FROM Details d " +
            "JOIN ProfileDetails pd ON d.id = pd.detailsId " +
            "JOIN UserProfile up ON pd.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY d.id DESC " +
            "LIMIT 1")
    Optional<Details> findLatestDetailsByUserId(@Param("userId") Long userId);


    /**
     * Find all experience records for a user
     */
    @Query("SELECT new com.example.demo.model.Experience(e.id, e.roleName, e.dateFrom, e.dateTo, e.companyName, e.countryCity, e.service) " +
            "FROM Experience e " +
            "JOIN ProfileExperience pe ON e.id = pe.experienceId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Experience> findExperienceByUserId(@Param("userId") Long userId);

    /**
     * Find the latest experience record for a user
     * Optimized: Single record retrieval
     */
    @Query("SELECT new com.example.demo.model.Experience(e.id, e.roleName, e.dateFrom, e.dateTo, e.companyName, e.countryCity, e.service) " +
            "FROM Experience e " +
            "JOIN ProfileExperience pe ON e.id = pe.experienceId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY e.id DESC " +
            "LIMIT 1")
    Optional<Experience> findLatestExperienceByUserId(@Param("userId") Long userId);

    /**
     * Find the most recent experience by date
     */
    @Query("SELECT new com.example.demo.model.Experience(e.id, e.roleName, e.dateFrom, e.dateTo, e.companyName, e.countryCity, e.service) " +
            "FROM Experience e " +
            "JOIN ProfileExperience pe ON e.id = pe.experienceId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY e.dateTo DESC, e.id DESC " +
            "LIMIT 1")
    Optional<Experience> findMostRecentExperienceByUserId(@Param("userId") Long userId);


    /**
     * Find all skills records for a user
     */
    @Query("SELECT new com.example.demo.model.Skills(s.id, s.programmingLanguages, s.webFrameworks, s.devOps, s.sql, s.vcs, s.tools) " +
            "FROM Skills s " +
            "JOIN ProfileSkills ps ON s.id = ps.skillsId " +
            "JOIN UserProfile up ON ps.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Skills> findSkillsByUserId(@Param("userId") Long userId);

    /**
     * Find the latest skills record for a user
     * Optimized: Single record retrieval
     */
    @Query("SELECT new com.example.demo.model.Skills(s.id, s.programmingLanguages, s.webFrameworks, s.devOps, s.sql, s.vcs, s.tools) " +
            "FROM Skills s " +
            "JOIN ProfileSkills ps ON s.id = ps.skillsId " +
            "JOIN UserProfile up ON ps.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY s.id DESC " +
            "LIMIT 1")
    Optional<Skills> findLatestSkillsByUserId(@Param("userId") Long userId);


    /**
     * Find all certificates for a user
     */
    @Query("SELECT new com.example.demo.model.Certificate(c.id, c.certificateName, c.dateFrom, c.dateTo, c.countryCity, c.institutionName, c.certificateUrl) " +
            "FROM Certificate c " +
            "JOIN ProfileCertificate pc ON c.id = pc.certificateId " +
            "JOIN UserProfile up ON pc.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Certificate> findCertificatesByUserId(@Param("userId") Long userId);

    /**
     * Find the latest certificate for a user
     * Optimized: Single record retrieval
     */
    @Query("SELECT new com.example.demo.model.Certificate(c.id, c.certificateName, c.dateFrom, c.dateTo, c.countryCity, c.institutionName, c.certificateUrl) " +
            "FROM Certificate c " +
            "JOIN ProfileCertificate pc ON c.id = pc.certificateId " +
            "JOIN UserProfile up ON pc.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY c.id DESC " +
            "LIMIT 1")
    Optional<Certificate> findLatestCertificateByUserId(@Param("userId") Long userId);

    /**
     * Find the most recent certificate by date
     */
    @Query("SELECT new com.example.demo.model.Certificate(c.id, c.certificateName, c.dateFrom, c.dateTo, c.countryCity, c.institutionName, c.certificateUrl) " +
            "FROM Certificate c " +
            "JOIN ProfileCertificate pc ON c.id = pc.certificateId " +
            "JOIN UserProfile up ON pc.profileId = up.profileId " +
            "WHERE up.userId = :userId " +
            "ORDER BY c.dateTo DESC, c.id DESC " +
            "LIMIT 1")
    Optional<Certificate> findMostRecentCertificateByUserId(@Param("userId") Long userId);


    /**
     * Count total projects for a user
     */
    @Query("SELECT COUNT(pr) FROM Project pr " +
            "JOIN ProfileProject pp ON pr.id = pp.projectId " +
            "JOIN UserProfile up ON pp.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    long countProjectsByUserId(@Param("userId") Long userId);

    /**
     * Count total certificates for a user
     */
    @Query("SELECT COUNT(c) FROM Certificate c " +
            "JOIN ProfileCertificate pc ON c.id = pc.certificateId " +
            "JOIN UserProfile up ON pc.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    long countCertificatesByUserId(@Param("userId") Long userId);

    /**
     * Count total experience records for a user
     */
    @Query("SELECT COUNT(e) FROM Experience e " +
            "JOIN ProfileExperience pe ON e.id = pe.experienceId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    long countExperiencesByUserId(@Param("userId") Long userId);
}