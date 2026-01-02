/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.repository;

import com.example.demo.model.ProfileNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileNotificationRepository extends JpaRepository<ProfileNotification, Long> {

}
