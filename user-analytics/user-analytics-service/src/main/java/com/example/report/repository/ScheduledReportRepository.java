/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.report.repository;

import com.example.report.model.ScheduledReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledReportRepository extends JpaRepository<ScheduledReport, String> {

    List<ScheduledReport> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<ScheduledReport> findByEnabledTrueAndNextRunBefore(LocalDateTime dateTime);

    @Query("SELECT sr FROM ScheduledReport sr WHERE sr.userId = :userId AND sr.enabled = true")
    List<ScheduledReport> findActiveSchedules(@Param("userId") Long userId);

    void deleteByIdAndUserId(String id, Long userId);

    Long countByUserIdAndEnabledTrue(Long userId);
}
