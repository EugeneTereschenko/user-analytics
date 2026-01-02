package com.example.demo.dto;

import lombok.NonNull;
import lombok.ToString;

import java.util.Collection;

@ToString
public class UserRequestDTO {

    private String username;
    private String email;
    @NonNull
    private String password;
    private String deviceType;
    private String location;
    private Collection<String> roles; // Add roles field


    public UserRequestDTO() {
    }

    public UserRequestDTO(String username, String email, String password, String deviceType, String location, Collection<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.deviceType = deviceType;
        this.location = location;
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

    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        private String deviceType;
        private String location;
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

        public Builder deviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public Builder roles(Collection<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public UserRequestDTO build() {
            return new UserRequestDTO(username, email, password, deviceType, location, roles);
        }
    }
}
