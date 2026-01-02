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
public class ProfileEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;

    @Column(name = "education_id", length = 255)
    private Long educationId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long educationId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder educationId(Long educationId) {
            this.educationId = educationId;
            return this;
        }

        public ProfileEducation build() {
            ProfileEducation profileEducation = new ProfileEducation();
            profileEducation.profileId = this.profileId;
            profileEducation.educationId = this.educationId;
            return profileEducation;
        }
    }
}
