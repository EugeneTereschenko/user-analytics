package com.example.activity.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 255)
    private ActivityType type;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @CreatedDate
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device_type", length = 255)
    private String deviceType;

    @Column(name = "location")
    private String location;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    public static class Builder {
        private Long userId;
        private String username;
        private ActivityType type;
        private String description;
        private String ipAddress;
        private String deviceType;
        private String location;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder type(ActivityType type) {
            this.type = type;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder deviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Activity build() {
            Activity activity = new Activity();
            activity.userId = this.userId;
            activity.username = this.username;
            activity.type = this.type;
            activity.description = this.description;
            activity.ipAddress = this.ipAddress;
            activity.deviceType = this.deviceType;
            activity.location = this.location;
            activity.timestamp = LocalDateTime.now();
            return activity;
        }
    }
}
