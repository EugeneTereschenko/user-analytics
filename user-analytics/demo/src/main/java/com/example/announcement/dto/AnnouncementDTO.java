/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.announcement.dto;

import com.example.announcement.model.AnnouncementCategory;
import com.example.announcement.model.AnnouncementPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {
    private Long id;
    private String title;
    private String body;
    private String date;
    private String author;
    private AnnouncementPriority priority;
    private AnnouncementCategory category;
    private Boolean isRead;
    private String expiryDate;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String title;
        private String body;
        private String date;
        private String author;
        private AnnouncementPriority priority;
        private AnnouncementCategory category;
        private Boolean isRead;
        private String expiryDate;

        public Builder id(Long id) {
            this.id = id;
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

        public Builder date(String date) {
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

        public Builder isRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public Builder expiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public AnnouncementDTO build() {
            return new AnnouncementDTO(id, title, body, date, author,
                    priority, category, isRead, expiryDate);
        }
    }
}
