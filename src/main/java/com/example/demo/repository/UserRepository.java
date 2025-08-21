package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    long countBySignupDate(LocalDate date);

    @Query("SELECT u FROM User u ORDER BY u.loginCount DESC LIMIT 1")
    Optional<User> findMostActiveUser();

    @Query("SELECT COUNT(u) FROM User u WHERE u.signupDate >= :startDate")
    long countSignupsSince(@Param("startDate") LocalDate startDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLogin >= :startDateTime")
    long countActiveUsersSince(@Param("startDateTime") LocalDateTime startDateTime);

}
