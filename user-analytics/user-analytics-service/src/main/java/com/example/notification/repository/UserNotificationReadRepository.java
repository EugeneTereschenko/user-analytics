/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.repository;

import com.example.notification.model.UserNotificationRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotificationReadRepository extends JpaRepository<UserNotificationRead, Long> {

    List<UserNotificationRead> findByUserId(Long userId);

    Optional<UserNotificationRead> findByUserIdAndNotificationId(Long userId, Long notificationId);

    boolean existsByUserIdAndNotificationId(Long userId, Long notificationId);

    List<UserNotificationRead> findByNotificationId(Long notificationId);
}
