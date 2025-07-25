package com.example.demo.repository;

import com.example.demo.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Optional<Reminder> findByTitle(String title);


    @Query("SELECT new com.example.demo.model.Reminder(re.id, re.title, re.date, re.time, re.notified) " +
            "FROM Reminder re " +
            "JOIN ProfileReminder pr ON pr.reminderId = re.id " +
            "JOIN UserProfile up ON pr.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Reminder> findRemindersByUserId(@Param("userId") Long userId);

}
