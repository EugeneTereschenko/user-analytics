package com.example.demo.repository;

import com.example.demo.model.Image;
import com.example.demo.model.Profile;
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
}
