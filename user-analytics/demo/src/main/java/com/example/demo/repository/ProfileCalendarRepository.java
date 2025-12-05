package com.example.demo.repository;

import com.example.demo.model.ProfileCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileCalendarRepository extends JpaRepository<ProfileCalendar, Long> {

}
