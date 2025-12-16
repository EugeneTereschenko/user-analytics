/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.announcement.repository;

import com.example.announcement.model.Announcement;
import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByPriority(AnnouncementPriority priority);

    List<Announcement> findByCategory(AnnouncementCategory category);

    List<Announcement> findByIsActiveTrue();

    @Query("SELECT a FROM Announcement a WHERE a.isActive = true AND " +
            "(a.expiryDate IS NULL OR a.expiryDate > :now)")
    List<Announcement> findActiveNonExpired(@Param("now") LocalDateTime now);

    @Query("SELECT a FROM Announcement a WHERE " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.body) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Announcement> searchAnnouncements(@Param("query") String query);

    List<Announcement> findAllByOrderByDateDesc();

    @Query("SELECT a FROM Announcement a WHERE a.priority = :priority " +
            "ORDER BY a.date DESC")
    List<Announcement> findByPriorityOrderByDateDesc(@Param("priority") AnnouncementPriority priority);
}
