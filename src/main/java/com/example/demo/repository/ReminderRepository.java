package com.example.demo.repository;

import com.example.demo.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Optional<Reminder> findByTitle(String title);

}
