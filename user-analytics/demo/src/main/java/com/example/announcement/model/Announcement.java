package com.example.announcement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "announcements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "body", nullable = false, length = 2000)
    private String body;

    @Column(name = "date")
    private Date date;

    @Column(name = "author", length = 100)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private AnnouncementPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private AnnouncementCategory category;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (date == null) {
            date = new Date();
        }
        if (priority == null) {
            priority = AnnouncementPriority.MEDIUM;
        }
        if (category == null) {
            category = AnnouncementCategory.GENERAL;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static class Builder {
        private Long id;
        private Long version;
        private String title;
        private String body;
        private Date date;
        private String author;
        private AnnouncementPriority priority;
        private AnnouncementCategory category;
        private Boolean isActive;
        private LocalDateTime expiryDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder priority(AnnouncementPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder category(AnnouncementCategory category) {
            this.category = category;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder expiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Announcement build() {
            Announcement announcement = new Announcement();
            announcement.id = this.id;
            announcement.version =  this.version;
            announcement.title = this.title;
            announcement.body = this.body;
            announcement.date = this.date;
            announcement.author = this.author;
            announcement.priority = this.priority;
            announcement.category = this.category;
            announcement.isActive = this.isActive;
            announcement.expiryDate = this.expiryDate;
            return announcement;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
