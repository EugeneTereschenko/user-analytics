package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_education")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;

    @Column(name = "experience_id", length = 255)
    private Long experienceId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long experienceId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder experienceId(Long experienceId) {
            this.experienceId = experienceId;
            return this;
        }

        public ProfileExperience build() {
            ProfileExperience profileExperience = new ProfileExperience();
            profileExperience.profileId = this.profileId;
            profileExperience.experienceId = this.experienceId;
            return profileExperience;
        }
    }
}
