/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.demo.testutil;

import com.example.announcement.model.Announcement;
import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

public class AnnouncementTestDataBuilder {


    private Long id = 1L;
    private String title = "Test Title";
    private String body = "Test Body";
    private AnnouncementCategory category = AnnouncementCategory.GENERAL;
    private AnnouncementPriority priority = AnnouncementPriority.MEDIUM;
    private Boolean isActive = true;
    private LocalDateTime date = LocalDateTime.now();
    private LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);

    public AnnouncementTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AnnouncementTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public AnnouncementTestDataBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public AnnouncementTestDataBuilder withCategory(AnnouncementCategory category) {
        this.category = category;
        return this;
    }

    public AnnouncementTestDataBuilder withPriority(AnnouncementPriority priority) {
        this.priority = priority;
        return this;
    }

    public AnnouncementTestDataBuilder withIsActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public AnnouncementTestDataBuilder withDate(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public AnnouncementTestDataBuilder withExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public Announcement build() {
        Announcement announcement = new Announcement();
       // announcement.setId(id);
        announcement.setTitle(title);
        announcement.setBody(body);
        announcement.setCategory(category);
        announcement.setPriority(priority);
        announcement.setIsActive(isActive);
        announcement.setDate(java.sql.Timestamp.valueOf(date));
        announcement.setExpiryDate(expiryDate);
        return announcement;
    }
}
