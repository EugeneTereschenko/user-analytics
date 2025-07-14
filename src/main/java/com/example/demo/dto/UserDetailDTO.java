package com.example.demo.dto;

import lombok.Data;

@Data
public class UserDetailDTO {
    private String name;
    private String email;
    private String role;

    public static class Builder {
        private String name;
        private String email;
        private String role;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public UserDetailDTO build() {
            UserDetailDTO userDetailDTO = new UserDetailDTO();
            userDetailDTO.name = this.name;
            userDetailDTO.email = this.email;
            userDetailDTO.role = this.role;
            return userDetailDTO;
        }
    }
}
