package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_calendar")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "profile_id")
    private Long profileId;
    @Column(name = "calendar_id")
    private Long calendarId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long calendarId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder calendarId(Long calendarId) {
            this.calendarId = calendarId;
            return this;
        }

        public ProfileCalendar build() {
            ProfileCalendar profileCalendar = new ProfileCalendar();
            profileCalendar.profileId = this.profileId;
            profileCalendar.calendarId = this.calendarId;
            return profileCalendar;
        }
    }
}
