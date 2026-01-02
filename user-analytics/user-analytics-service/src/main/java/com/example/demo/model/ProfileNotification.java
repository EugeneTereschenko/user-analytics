package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_notification")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    Long profileId;

    @Column(name = "notification_id", length = 255)
    Long notificationId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long notificationId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder notificationId(Long notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public ProfileNotification build() {
            ProfileNotification profileNotification = new ProfileNotification();
            profileNotification.profileId = this.profileId;
            profileNotification.notificationId = this.notificationId;
            return profileNotification;
        }
    }
}
