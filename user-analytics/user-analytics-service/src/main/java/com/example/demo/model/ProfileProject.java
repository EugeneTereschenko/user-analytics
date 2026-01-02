package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_project")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;

    @Column(name = "project_id", length = 255)
    private Long projectId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long projectId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder projectId(Long projectId) {
            this.projectId = projectId;
            return this;
        }

        public ProfileProject build() {
            ProfileProject profileProject = new ProfileProject();
            profileProject.profileId = this.profileId;
            profileProject.projectId = this.projectId;
            return profileProject;
        }
    }
}
