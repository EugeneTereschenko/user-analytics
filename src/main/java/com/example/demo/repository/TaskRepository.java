package com.example.demo.repository;

import com.example.demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTitle(String title);

    @Query("SELECT new com.example.demo.model.Task(ta.id, ta.title, ta.done) " +
            "FROM Task ta " +
            "JOIN ProfileTask pr ON pr.taskId = ta.id " +
            "JOIN UserProfile up ON pr.profileId = up.profileId " +
            "WHERE up.userId = :userId")
    List<Task> findTasksByUserId(@Param("userId") Long userId);

}
