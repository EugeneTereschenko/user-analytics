/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.report.repository;

import com.example.report.model.SavedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SavedReportRepository extends JpaRepository<SavedReport, String> {

    List<SavedReport> findByUserIdOrderBySavedAtDesc(Long userId);

    List<SavedReport> findByUserIdAndTypeOrderBySavedAtDesc(Long userId, String type);

    @Query("SELECT sr FROM SavedReport sr WHERE sr.userId = :userId AND sr.savedAt >= :startDate")
    List<SavedReport> findRecentReports(@Param("userId") Long userId,
                                        @Param("startDate") LocalDateTime startDate);

    void deleteByIdAndUserId(String id, Long userId);

    Long countByUserId(Long userId);
}
