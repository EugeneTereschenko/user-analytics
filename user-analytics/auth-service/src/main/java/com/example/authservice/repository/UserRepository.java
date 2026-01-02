/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.authservice.repository;

import com.example.authservice.model.User;
import com.example.authservice.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByPasswordResetToken(String token);

    Optional<User> findByVerificationToken(String token);

    List<User> findByUserType(UserType userType);

    List<User> findByIsActive(Boolean isActive);

    @Query("SELECT u FROM User u WHERE u.isLocked = true AND u.updatedAt < :since")
    List<User> findLockedUsersSince(@Param("since") LocalDateTime since);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
}
