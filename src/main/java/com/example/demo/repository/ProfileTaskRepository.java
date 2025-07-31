package com.example.demo.repository;

import com.example.demo.model.ProfileTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileTaskRepository extends JpaRepository<ProfileTask, Long> {

}
