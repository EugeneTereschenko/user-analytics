package com.example.demo.dto;

import lombok.Data;

@Data
public class NotificationsDTO {
    public String title;
    public String timestamp;
    public String message;

    public static class Builder {
        private String title;
        private String timestamp;
        private String message;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationsDTO build() {
            NotificationsDTO notificationsDTO = new NotificationsDTO();
            notificationsDTO.title = this.title;
            notificationsDTO.timestamp = this.timestamp;
            notificationsDTO.message = this.message;
            return notificationsDTO;
        }
    }
}
