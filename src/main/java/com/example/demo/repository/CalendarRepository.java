package com.example.demo.repository;

import com.example.demo.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByTitle(String title);

    @Query("SELECT new com.example.demo.model.Calendar(ca.id, ca.title, ca.date) " +
            "FROM Calendar ca " +
            "JOIN ProfileCalendar pc ON pc.calendarId = ca.id " +
            "JOIN UserProfile up ON pc.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Calendar> findCalendarsByUserId(@Param("userId") Long userId);
}
