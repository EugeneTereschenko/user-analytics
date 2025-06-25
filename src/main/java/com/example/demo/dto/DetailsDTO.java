package com.example.demo.dto;

import lombok.Data;

@Data
public class DetailsDTO {
    private String notification;
    private String staff;
    private String bio;
    private String message;

    public static DetailsDTO.Builder builder() {
        return new DetailsDTO.Builder();
    }

    public static class Builder {
        private String notification;
        private String staff;
        private String bio;
        private String message;

        public Builder notification(String notification) {
            this.notification = notification;
            return this;
        }

        public Builder staff(String staff) {
            this.staff = staff;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public DetailsDTO build() {
            DetailsDTO detailsDTO = new DetailsDTO();
            detailsDTO.setNotification(this.notification);
            detailsDTO.setStaff(this.staff);
            detailsDTO.setBio(this.bio);
            detailsDTO.setMessage(this.message);
            return detailsDTO;
        }
    }
}
