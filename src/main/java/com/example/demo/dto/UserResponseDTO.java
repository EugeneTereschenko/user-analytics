package com.example.demo.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String twoFactor;
    private String message;
    private String success;
    private String token;

    public UserResponseDTO() {
    }
    public UserResponseDTO(String twoFactor, String message, String success, String token) {
        this.twoFactor = twoFactor;
        this.message = message;
        this.success = success;
        this.token = token;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String twoFactor;
        private String message;
        private String success;
        private String token;

        public Builder twoFactor(String twoFactor) {
            this.twoFactor = twoFactor;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder success(String success) {
            this.success = success;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public UserResponseDTO build() {
            return new UserResponseDTO(twoFactor, message, success, token);
        }
    }
}
