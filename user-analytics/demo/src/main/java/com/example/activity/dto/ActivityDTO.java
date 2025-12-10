package com.example.activity.dto;

import com.example.activity.model.ActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private Long id;
    private Long userId;
    private String username;
    private ActivityType type;
    private String description;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String deviceType;
    private String location;

    public static class Builder {
        private Long id;
        private Long userId;
        private String username;
        private ActivityType type;
        private String description;
        private LocalDateTime timestamp;
        private String ipAddress;
        private String deviceType;
        private String location;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
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

        public ActivityDTO build() {
            return new ActivityDTO(id, userId, username, type, description,
                    timestamp, ipAddress, deviceType, location);
        }
    }
}
