/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.activity.repository;
import com.example.activity.model.Activity;
import com.example.activity.model.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByUserId(Long userId);

    List<Activity> findByType(ActivityType type);

    List<Activity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<Activity> findTop10ByOrderByTimestampDesc();

    @Query("SELECT a FROM Activity a ORDER BY a.timestamp DESC")
    List<Activity> findAllOrderByTimestampDesc();

    @Query("SELECT COUNT(a) FROM Activity a WHERE a.type = :type")
    long countByType(@Param("type") ActivityType type);

    @Query("SELECT a FROM Activity a WHERE a.userId = :userId ORDER BY a.timestamp DESC")
    List<Activity> findByUserIdOrderByTimestampDesc(@Param("userId") Long userId);


    List<Activity> findByUsernameAndTimestampBetween(String username,
                                                     LocalDateTime start,
                                                     LocalDateTime end);

    @Query("SELECT a.username, COUNT(a) FROM Activity a " +
            "WHERE a.timestamp BETWEEN :start AND :end " +
            "GROUP BY a.username " +
            "ORDER BY COUNT(a) DESC")
    List<Object[]> getUserActivityCounts(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    @Query("SELECT a.type, COUNT(a) FROM Activity a " +
            "WHERE a.timestamp BETWEEN :start AND :end " +
            "GROUP BY a.type")
    List<Object[]> getActivityTypeCounts(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    Long countByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
