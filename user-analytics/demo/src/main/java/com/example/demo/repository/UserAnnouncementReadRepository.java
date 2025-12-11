/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.demo.repository;

import com.example.announcement.model.UserAnnouncementRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnnouncementReadRepository extends JpaRepository<UserAnnouncementRead, Long> {

    List<UserAnnouncementRead> findByUserId(Long userId);

    Optional<UserAnnouncementRead> findByUserIdAndAnnouncementId(Long userId, Long announcementId);

    boolean existsByUserIdAndAnnouncementId(Long userId, Long announcementId);

    void deleteByUserId(Long userId);
}
