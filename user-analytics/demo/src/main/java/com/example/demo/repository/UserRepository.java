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

    // ========== Existing Methods (Keep these) ==========

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // ========== Analytics Methods ==========

    /**
     * Count users created between two timestamps
     * Used for: Today's signups, weekly signups, etc.
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    int countByCreatedAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Count users created after a specific timestamp
     * Used for: Users this week, this month, etc.
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :date")
    int countByCreatedAtAfter(@Param("date") LocalDateTime date);

    /**
     * Count active users in the last week
     * Note: Requires a 'lastLoginAt' field in User entity
     * If you don't have this field, see alternative implementations below
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLoginAt >= :weekAgo")
    int countActiveUsersLastWeek(@Param("weekAgo") LocalDateTime weekAgo);

    /**
     * Find the most active user
     * Note: Requires an 'activityCount' or 'loginCount' field
     * If you don't have this, see alternative implementations below
     */
    @Query("SELECT u FROM User u ORDER BY u.activityCount DESC LIMIT 1")
    Optional<User> findMostActiveUser();

    /**
     * Find top N active users
     * Returns usernames of most active users
     */
    @Query("SELECT u.username FROM User u ORDER BY u.activityCount DESC LIMIT :limit")
    List<String> findTopActiveUsers(@Param("limit") int limit);

    // ========== Alternative Implementations ==========

    /**
     * ALTERNATIVE: If you don't have lastLoginAt field
     * Count users created in last week as "active"
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :weekAgo")
    int countRecentUsersLastWeek(@Param("weekAgo") LocalDateTime weekAgo);

    /**
     * ALTERNATIVE: Find most recently created user
     */
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC LIMIT 1")
    Optional<User> findMostRecentUser();

    /**
     * ALTERNATIVE: Top users by creation date (newest first)
     */
    @Query("SELECT u.username FROM User u ORDER BY u.createdAt DESC LIMIT :limit")
    List<String> findMostRecentUsers(@Param("limit") int limit);

    // ========== Additional Useful Analytics Methods ==========

    /**
     * Count users by role
     */
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    int countByRole(@Param("roleName") String roleName);

    /**
     * Count users by location
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.location = :location")
    int countByLocation(@Param("location") String location);

    /**
     * Count users by device type
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.deviceType = :deviceType")
    int countByDeviceType(@Param("deviceType") String deviceType);

    /**
     * Find users created today
     */
    @Query("SELECT u FROM User u WHERE u.createdAt >= :startOfDay")
    List<User> findUsersCreatedToday(@Param("startOfDay") LocalDateTime startOfDay);

    /**
     * Get user growth statistics by month
     */
    @Query("SELECT FUNCTION('DATE_TRUNC', 'month', u.createdAt) as month, COUNT(u) " +
            "FROM User u " +
            "WHERE u.createdAt >= :startDate " +
            "GROUP BY FUNCTION('DATE_TRUNC', 'month', u.createdAt) " +
            "ORDER BY month DESC")
    List<Object[]> getUserGrowthByMonth(@Param("startDate") LocalDateTime startDate);

    /**
     * Get user distribution by location
     */
    @Query("SELECT u.location, COUNT(u) FROM User u GROUP BY u.location")
    List<Object[]> getUserDistributionByLocation();

    /**
     * Get user distribution by device type
     */
    @Query("SELECT u.deviceType, COUNT(u) FROM User u GROUP BY u.deviceType")
    List<Object[]> getUserDistributionByDeviceType();

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
