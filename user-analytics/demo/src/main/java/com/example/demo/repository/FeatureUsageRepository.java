/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.demo.repository;

import com.example.featureusage.model.FeatureUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeatureUsageRepository extends JpaRepository<FeatureUsage, Long> {

    List<FeatureUsage> findByUserId(Long userId);

    List<FeatureUsage> findByFeatureName(String featureName);

    List<FeatureUsage> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT fu.featureName, COUNT(fu) as usageCount FROM FeatureUsage fu " +
            "WHERE fu.timestamp >= :start " +
            "GROUP BY fu.featureName ORDER BY usageCount DESC")
    List<Object[]> getFeatureUsageStats(@Param("start") LocalDateTime start);

    @Query("SELECT fu.category, COUNT(fu) FROM FeatureUsage fu " +
            "GROUP BY fu.category")
    List<Object[]> getUsageByCategory();

    @Query("SELECT COUNT(DISTINCT fu.userId) FROM FeatureUsage fu " +
            "WHERE fu.timestamp >= :start")
    Long countActiveUsers(@Param("start") LocalDateTime start);

    @Query("SELECT COUNT(DISTINCT fu.sessionId) FROM FeatureUsage fu")
    Long countTotalSessions();

    @Query("SELECT AVG(fu.durationSeconds) FROM FeatureUsage fu " +
            "WHERE fu.durationSeconds IS NOT NULL")
    Double getAverageSessionDuration();

    @Query("SELECT fu.featureName, COUNT(DISTINCT fu.userId) FROM FeatureUsage fu " +
            "WHERE fu.timestamp >= :start " +
            "GROUP BY fu.featureName")
    List<Object[]> getUniqueUsersPerFeature(@Param("start") LocalDateTime start);

    @Query("SELECT fu.featureName, AVG(fu.durationSeconds) FROM FeatureUsage fu " +
            "WHERE fu.durationSeconds IS NOT NULL " +
            "GROUP BY fu.featureName")
    List<Object[]> getAvgTimePerFeature();
}
