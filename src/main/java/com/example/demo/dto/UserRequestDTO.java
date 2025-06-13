package com.example.demo.dto;

import lombok.ToString;

import java.util.Collection;

@ToString
public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    private Collection<String> roles; // Add roles field


    public UserRequestDTO() {
    }

    public UserRequestDTO(String username, String email, String password, Collection<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles; // Initialize roles
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<String> getRoles() {
        return roles;
    }
    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String username;
        private String email;
        private String password;
        private Collection<String> roles;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder roles(Collection<String> roles) {
            this.roles = roles;
            return this;
        }

        public UserRequestDTO build() {
            return new UserRequestDTO(username, email, password, roles);
        }
    }
}
