package com.example.demo.repository;

import com.example.demo.model.ProfileReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileReminderRepository extends JpaRepository<ProfileReminder, Long> {
    public void deleteByReminderId(Long reminderId);
}
