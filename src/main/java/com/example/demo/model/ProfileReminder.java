package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_reminder")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileReminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "project_id")
    private Long reminderId;


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long reminderId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder reminderId(Long reminderId) {
            this.reminderId = reminderId;
            return this;
        }

        public ProfileReminder build() {
            ProfileReminder profileReminder = new ProfileReminder();
            profileReminder.profileId = this.profileId;
            profileReminder.reminderId = this.reminderId;
            return profileReminder;
        }
    }
}
