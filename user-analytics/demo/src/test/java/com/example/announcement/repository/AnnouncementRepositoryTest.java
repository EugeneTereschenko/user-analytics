/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

// File: demo/src/test/java/com/example/announcement/repository/AnnouncementRepositoryTest.java
package com.example.announcement.repository;

import com.example.announcement.model.Announcement;
import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;
import com.example.demo.UserAnalyticsJavaApplication;
import com.example.demo.testutil.AnnouncementTestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {UserAnalyticsJavaApplication.class})
class AnnouncementRepositoryTest {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Test
    @DisplayName("Should find by priority")
    void testFindByPriority() {
        Announcement a = new AnnouncementTestDataBuilder()
                .withTitle("Test")
                .withPriority(AnnouncementPriority.HIGH)
                .withIsActive(true)
                .build();
        announcementRepository.save(a);

        List<Announcement> found = announcementRepository.findByPriority(AnnouncementPriority.HIGH);
        assertThat(found).extracting(Announcement::getPriority).containsOnly(AnnouncementPriority.HIGH);
    }

    @Test
    @DisplayName("Should find by category")
    void testFindByCategory() {
        Announcement a = new AnnouncementTestDataBuilder()
            .withTitle("Test")
            .withCategory(AnnouncementCategory.GENERAL)
            .withIsActive(true)
            .build();
        announcementRepository.save(a);

        List<Announcement> found = announcementRepository.findByCategory(AnnouncementCategory.GENERAL);
        assertThat(found).extracting(Announcement::getCategory).containsOnly(AnnouncementCategory.GENERAL);
    }

    @Test
    @DisplayName("Should find active announcements")
    void testFindByIsActiveTrue() {
        Announcement a = new AnnouncementTestDataBuilder()
                .withTitle("Test")
                .withIsActive(true)
                .build();

        announcementRepository.save(a);

        List<Announcement> found = announcementRepository.findByIsActiveTrue();
        assertThat(found).allMatch(Announcement::getIsActive);
    }

    @Test
    @DisplayName("Should find active and non-expired announcements")
    void testFindActiveNonExpired() {
        Announcement a = new AnnouncementTestDataBuilder()
            .withIsActive(true)
            .withExpiryDate(LocalDateTime.now().plusDays(1))
            .build();

        announcementRepository.save(a);

        List<Announcement> found = announcementRepository.findActiveNonExpired(LocalDateTime.now());
        assertThat(found).isNotEmpty();
    }

    @Test
    @DisplayName("Should search announcements by query")
    void testSearchAnnouncements() {
        Announcement a = new AnnouncementTestDataBuilder()
                .withTitle("Test Title")
                .withBody("Test Body")
                .withIsActive(true)
                .build();

        announcementRepository.save(a);

        List<Announcement> found = announcementRepository.searchAnnouncements("test");
        assertThat(found).isNotEmpty();
    }

    @Test
    @DisplayName("Should find all by order by date desc")
    void testFindAllByOrderByDateDesc() {
        Announcement a1 = new AnnouncementTestDataBuilder()
            .withDate(LocalDateTime.now().minusDays(1))
            .withIsActive(true)
            .build();


        Announcement a2 = new AnnouncementTestDataBuilder()
            .withDate(LocalDateTime.now())
            .withIsActive(true)
            .build();


        announcementRepository.save(a1);
        announcementRepository.save(a2);

        List<Announcement> found = announcementRepository.findAllByOrderByDateDesc();
        assertThat(found.get(0).getDate()).isAfterOrEqualTo(found.get(1).getDate());
    }

    @Test
    @DisplayName("Should find by priority and order by date desc")
    void testFindByPriorityOrderByDateDesc() {
        Announcement a1 = new AnnouncementTestDataBuilder()
            .withPriority(AnnouncementPriority.HIGH)
            .withDate(LocalDateTime.now())
            .withIsActive(true)
            .build();


        Announcement a2 = new AnnouncementTestDataBuilder()
            .withPriority(AnnouncementPriority.HIGH)
            .withDate(LocalDateTime.now().minusDays(1))
            .withIsActive(true)
            .build();

        announcementRepository.save(a1);
        announcementRepository.save(a2);

        List<Announcement> found = announcementRepository.findByPriorityOrderByDateDesc(AnnouncementPriority.HIGH);
        assertThat(found.get(0).getDate()).isAfterOrEqualTo(found.get(1).getDate());
    }
}
