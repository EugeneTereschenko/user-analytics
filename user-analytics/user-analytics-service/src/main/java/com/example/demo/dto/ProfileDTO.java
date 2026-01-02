package com.example.demo.dto;

import lombok.Data;

@Data
public class ProfileDTO {
    public String firstName;
    public String lastName;
    public String email;
    public String linkedin;
    public String skype;
    public String github;
    public String address;
    public String shippingAddress;
    public String phone;
    public String recentProject;
    public String mostViewedProject;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String linkedin;
        private String skype;
        private String github;
        private String address;
        private String shippingAddress;
        private String phone;
        private String recentProject;
        private String mostViewedProject;

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder linkedin(String linkedin) {
            this.linkedin = linkedin;
            return this;
        }

        public Builder skype(String skype) {
            this.skype = skype;
            return this;
        }

        public Builder github(String github) {
            this.github = github;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder shippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder recentProject(String recentProject) {
            this.recentProject = recentProject;
            return this;
        }

        public Builder mostViewedProject(String mostViewedProject) {
            this.mostViewedProject = mostViewedProject;
            return this;
        }

        public ProfileDTO build() {
            ProfileDTO dto = new ProfileDTO();
            dto.firstName = this.firstName;
            dto.lastName = this.lastName;
            dto.email = this.email;
            dto.linkedin = this.linkedin;
            dto.skype = this.skype;
            dto.github = this.github;
            dto.address = this.address;
            dto.shippingAddress = this.shippingAddress;
            dto.phone = this.phone;
            dto.recentProject = this.recentProject;
            dto.mostViewedProject = this.mostViewedProject;
            return dto;
        }
    }
}
