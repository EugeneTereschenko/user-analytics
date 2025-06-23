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
    @Query("SELECT p FROM Profile p JOIN UserProfile up ON p.id = up.profileId WHERE up.userId = :userId")
    List<Profile> findProfilesByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.example.demo.model.Image(i.id, i.name, i.data) " +
            "FROM Image i " +
            "JOIN ProfileImage pi ON i.id = pi.imageId " +
            "JOIN UserProfile up ON pi.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Image> findImagesByUserId(@Param("userId") Long userId);

    @Query("Select pr " +
            "FROM Project pr " +
            "JOIN ProfileProject pp ON pr.Id = pp.projectId " +
            "JOIN UserProfile up ON pp.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);

    @Query("SELECT e" +
            " FROM Education e " +
            "JOIN ProfileEducation pe ON e.Id = pe.educationId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Education> findEducationByUserId(@Param("userId") Long userId);

    @Query("SELECT d " +
            "FROM Details d " +
            "JOIN ProfileDetails pd ON d.Id = pd.detailsId " +
            "JOIN UserProfile up ON pd.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Details> findDetailsByUserId(@Param("userId") Long userId);

    @Query("SELECT e " +
            "FROM Experience e " +
            "JOIN ProfileExperience pe ON e.Id = pe.experienceId " +
            "JOIN UserProfile up ON pe.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Experience> findExperienceByUserId(@Param("userId") Long userId);


    @Query("SELECT s " +
            "FROM Skills s " +
            "JOIN ProfileSkills ps ON s.Id = ps.skillsId " +
            "JOIN UserProfile up ON ps.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Skills> findSkillsByUserId(@Param("userId") Long userId);

}
