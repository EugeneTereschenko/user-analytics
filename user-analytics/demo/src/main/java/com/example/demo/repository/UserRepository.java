package com.example.demo.repository;

import com.example.demo.dto.UserSummaryDTO;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    long countBySignupDate(LocalDate date);

    @Query("SELECT u FROM User u ORDER BY u.loginCount DESC LIMIT 1")
    Optional<User> findMostActiveUser();

    @Query("SELECT COUNT(u) FROM User u WHERE u.signupDate >= :startDate")
    long countSignupsSince(@Param("startDate") LocalDate startDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLogin >= :startDateTime")
    long countActiveUsersSince(@Param("startDateTime") LocalDateTime startDateTime);

    @Query("SELECT new com.example.demo.dto.UserSummaryDTO(COUNT(u), SUM(u.loginCount), COUNT(DISTINCT u.deviceType), AVG(u.activityScore)) " +
            "FROM User u WHERE u.signupDate BETWEEN :start AND :end")
    UserSummaryDTO getUserSummary(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT u.signupDate, COUNT(u) FROM User u WHERE u.signupDate BETWEEN :start AND :end GROUP BY u.signupDate ORDER BY u.signupDate")
    List<Object[]> getSignups(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT u.deviceType, COUNT(u) FROM User u GROUP BY u.deviceType")
    List<Object[]> getDeviceBreakdown();

    @Query("SELECT u.location, COUNT(u) FROM User u GROUP BY u.location")
    List<Object[]> getUserLocations();

}
